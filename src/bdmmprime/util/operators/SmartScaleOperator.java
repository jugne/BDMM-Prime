package bdmmprime.util.operators;

import beast.core.Description;
import beast.core.Input;
import beast.core.Operator;
import beast.core.parameter.RealParameter;
import beast.util.Randomizer;

import java.util.*;

@Description("Scale operator which scales identical values together.")
public class SmartScaleOperator extends Operator {

    public Input<Double> scaleFactorInput = new Input<>("scaleFactor",
            "Scale factor will be chosen between scaleFactor and 1/scaleFactor.", 0.75);

    public Input<List<RealParameter>> parametersInput = new Input("parameter",
            "One or more parameters to operate on", new ArrayList<>());

    final public Input<Boolean> optimiseInput = new Input<>("optimise",
            "flag to indicate that the scale factor is automatically changed " +
                    "in order to achieve a good acceptance rate (default true)", true);

    public Input<Double> scaleFactorUpperLimitInput = new Input<>("scaleFactorUpperLimit",
            "Upper Limit of scale factor", 1.0 - 1e-8);
    public Input<Double> scaleFactorLowerLimitInput = new Input<>("scaleFactorLowerLimit",
            "Lower limit of scale factor", 1e-8);

    public Input<List<Double>> classesToExcludeInput = new Input<>("classToExclude",
            "Elements with this value will not be operated on.",
            new ArrayList<>());

    double scaleFactor, scaleFactorLowerLimit, scaleFactorUpperLimit;

    List<RealParameter> parameters;
    Map<RealParameter, Integer[]> groups;

    int nClasses;

    @Override
    public void initAndValidate() {

        parameters = parametersInput.get();
        scaleFactor = scaleFactorInput.get();

        scaleFactorLowerLimit = scaleFactorLowerLimitInput.get();
        scaleFactorUpperLimit = scaleFactorUpperLimitInput.get();

        SortedSet<Double> seenValuesSet = new TreeSet<>();

        for (RealParameter param : parameters) {
            for (int i=0; i<param.getDimension(); i++) {
                if (param.getValue(i) != 0.0)
                    seenValuesSet.add(param.getValue(i));
            }
        }

        // Explicitly exclude certain classes (identified by the element value)
        seenValuesSet.removeAll(classesToExcludeInput.get());

        List<Double> seenValues = new ArrayList<>(seenValuesSet);
        nClasses = seenValues.size();

        groups = new HashMap<>();

        for (RealParameter param : parameters) {
            Integer[] groupIDs = new Integer[param.getDimension()];

            for (int i = 0; i < param.getDimension(); i++)
                groupIDs[i] = seenValues.indexOf(param.getValue(i));

            groups.put(param, groupIDs);
        }
    }

    @Override
    public double proposal() {

        // Select class at random

        int classIdx = Randomizer.nextInt(nClasses);

        // Choose scale factor

        double minf = Math.min(scaleFactor, 1.0/scaleFactor);
        double f = minf + Randomizer.nextDouble()*(1.0/minf - minf);

        // Scale selected elements:

        for (RealParameter param : parameters) {
            Integer[] group = groups.get(param);

            for (int i=0; i<param.getDimension(); i++) {
                if (group[i] == classIdx) {
                    double newVal = f * param.getValue(i);
                    if (newVal < param.getLower() || newVal > param.getUpper())
                        return Double.NEGATIVE_INFINITY;
                    else
                        param.setValue(i, newVal);
                }
            }
        }

        // Hastings ratio for x -> f*x with f ~ [alpha,1/alpha] is 1/f.

        return -Math.log(f);
    }

    @Override
    public void optimize(double logAlpha) {
        if (optimiseInput.get()) {
            double delta = calcDelta(logAlpha);
            delta += Math.log(1.0/scaleFactor - 1.0);
            setCoercableParameterValue(1.0/(Math.exp(delta) + 1.0));
        }
    }

    @Override
    public double getCoercableParameterValue() {
        return scaleFactor;
    }

    @Override
    public void setCoercableParameterValue(double value) {
        scaleFactor = Math.max(Math.min(value, scaleFactorUpperLimit), scaleFactorLowerLimit);
    }
}
