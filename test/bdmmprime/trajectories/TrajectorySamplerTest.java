package bdmmprime.trajectories;

import bdmmprime.distributions.BirthDeathMigrationDistribution;
import bdmmprime.mapping.TypeMappedTree;
import bdmmprime.parameterization.*;
import bdmmprime.trajectories.simulation.SimulatedTree;
import bdmmprime.trajectories.simulation.UntypedTreeFromTypedTree;
import beast.core.parameter.RealParameter;
import beast.util.Randomizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrajectorySamplerTest {

    @Test
    public void untypedSimpleLikelihoodTest() {
        Randomizer.setSeed(53);
//        Randomizer.setSeed(42);

        Parameterization parameterization = new CanonicalParameterization();
        parameterization.initByName(
                "typeSet", new TypeSet(1),
                "origin", new RealParameter("5.0"),
                "finalSampleOffset", new RealParameter("0.0"),
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("2.0")),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.5")),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")));

        SimulatedTree simulatedTree = new SimulatedTree();
        simulatedTree.initByName(
                "parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "minSamples", 2);

//        System.out.println(simulatedTree);
        System.out.println("Final sample offset: " + parameterization.getFinalSampleOffset());

        SampledTrajectory sampledTrajectory = new SampledTrajectory();
        sampledTrajectory.initByName("typeMappedTree", simulatedTree,
                "parameterization", parameterization,
                "nParticles", 100000);

        double logProbEst = sampledTrajectory.getLogTreeProbEstimate();
        System.out.println("Log probability estimate: " + logProbEst);

        BirthDeathMigrationDistribution bdmm = new BirthDeathMigrationDistribution();
        bdmm.initByName("parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "typeLabel", "type",
                "conditionOnSurvival", false,
                "tree", simulatedTree);

        double logProbTrue = bdmm.calculateLogP();

        System.out.println("Log probability true: " + logProbTrue);

        assertEquals(logProbEst, logProbTrue, 1e-1);
    }

    @Test
    public void untypedSimpleTLLikelihoodTest() {
//        Randomizer.setSeed(53);
        Randomizer.setSeed(42);

        Parameterization parameterization = new CanonicalParameterization();
        parameterization.initByName(
                "typeSet", new TypeSet(1),
                "origin", new RealParameter("5.0"),
                "finalSampleOffset", new RealParameter("0.0"),
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("2.0")),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.5")),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")));

        SimulatedTree simulatedTree = new SimulatedTree();
        simulatedTree.initByName(
                "parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "minSamples", 2);

//        System.out.println(simulatedTree);
        System.out.println("Final sample offset: " + parameterization.getFinalSampleOffset());

        SampledTrajectory sampledTrajectory = new SampledTrajectory();
        sampledTrajectory.initByName("typeMappedTree", simulatedTree,
                "parameterization", parameterization,
                "nParticles", 10000,
                "useTauLeaping", true,
                "stepsPerInterval", 100);

        double logProbEst = sampledTrajectory.getLogTreeProbEstimate();
        System.out.println("Log probability estimate: " + logProbEst);

        BirthDeathMigrationDistribution bdmm = new BirthDeathMigrationDistribution();
        bdmm.initByName("parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "typeLabel", "type",
                "conditionOnSurvival", false,
                "tree", simulatedTree);

        double logProbTrue = bdmm.calculateLogP();

        System.out.println("Log probability true: " + logProbTrue);

        assertEquals(logProbEst, logProbTrue, 1e-1);
    }

    @Test
    public void untypedRateShiftLikelihoodTest() {
        Randomizer.setSeed(42);

        Parameterization parameterization = new CanonicalParameterization();
        parameterization.initByName(
                "typeSet", new TypeSet(1),
                "origin", new RealParameter("5.0"),
                "finalSampleOffset", new RealParameter("0.0"),
                "birthRate", new SkylineVectorParameter(
                        new RealParameter("2.5"),
                        new RealParameter("2.0 1.0"), 1),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")),
                "samplingRate", new SkylineVectorParameter(
                        new RealParameter("2"),
                        new RealParameter("0.0 0.5"), 1),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")));

        SimulatedTree simulatedTree = new SimulatedTree();
        simulatedTree.initByName(
                "parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "minSamples", 2);

//        System.out.println(simulatedTree);
        System.out.println("Final sample offset: " + parameterization.getFinalSampleOffset());

        SampledTrajectory sampledTrajectory = new SampledTrajectory();
        sampledTrajectory.initByName("typeMappedTree", simulatedTree,
                "parameterization", parameterization,
                "nParticles", 100000);

        double logProbEst = sampledTrajectory.getLogTreeProbEstimate();
        System.out.println("Log probability estimate: " + logProbEst);

        BirthDeathMigrationDistribution bdmm = new BirthDeathMigrationDistribution();
        bdmm.initByName("parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "typeLabel", "type",
                "conditionOnSurvival", false,
                "tree", simulatedTree);

        double logProbTrue = bdmm.calculateLogP();

        System.out.println("Log probability true: " + logProbTrue);

        assertEquals(logProbEst, logProbTrue, 1e-1);
    }


    @Test
    public void untypedRateShiftTLLikelihoodTest() {
        Randomizer.setSeed(42);

        Parameterization parameterization = new CanonicalParameterization();
        parameterization.initByName(
                "typeSet", new TypeSet(1),
                "origin", new RealParameter("5.0"),
                "finalSampleOffset", new RealParameter("0.0"),
                "birthRate", new SkylineVectorParameter(
                        new RealParameter("2.5"),
                        new RealParameter("2.0 1.0"), 1),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")),
                "samplingRate", new SkylineVectorParameter(
                        new RealParameter("2"),
                        new RealParameter("0.0 0.5"), 1),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")));

        SimulatedTree simulatedTree = new SimulatedTree();
        simulatedTree.initByName(
                "parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "minSamples", 2);

//        System.out.println(simulatedTree);
        System.out.println("Final sample offset: " + parameterization.getFinalSampleOffset());

        SampledTrajectory sampledTrajectory = new SampledTrajectory();
        sampledTrajectory.initByName("typeMappedTree", simulatedTree,
                "parameterization", parameterization,
                "nParticles", 10000,
                "useTauLeaping", true,
                "stepsPerInterval", 100);

        double logProbEst = sampledTrajectory.getLogTreeProbEstimate();
        System.out.println("Log probability estimate: " + logProbEst);

        BirthDeathMigrationDistribution bdmm = new BirthDeathMigrationDistribution();
        bdmm.initByName("parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "typeLabel", "type",
                "conditionOnSurvival", false,
                "tree", simulatedTree);

        double logProbTrue = bdmm.calculateLogP();

        System.out.println("Log probability true: " + logProbTrue);

        assertEquals(logProbEst, logProbTrue, 1e-1);
    }

    @Test
    public void untypedRhoSamplingLikelihoodTest() {
        Randomizer.setSeed(53);

        Parameterization parameterization = new CanonicalParameterization();
        parameterization.initByName(
                "typeSet", new TypeSet(1),
                "origin", new RealParameter("5.0"),
                "finalSampleOffset", new RealParameter("0.0"),
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("2.0")),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.5")),
                "rhoSampling", new TimedParameter(
                        new RealParameter("4.0"),
                        new RealParameter("0.5")),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0")));

        SimulatedTree simulatedTree = new SimulatedTree();
        simulatedTree.initByName(
                "parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "minSamples", 2);

        System.out.println(simulatedTree);
        System.out.println("Final sample offset: " + parameterization.getFinalSampleOffset());

        SampledTrajectory sampledTrajectory = new SampledTrajectory();
        sampledTrajectory.initByName("typeMappedTree", simulatedTree,
                "parameterization", parameterization,
                "nParticles", 100000);

        double logProbEst = sampledTrajectory.getLogTreeProbEstimate();
        System.out.println("Log probability estimate: " + logProbEst);

        BirthDeathMigrationDistribution bdmm = new BirthDeathMigrationDistribution();
        bdmm.initByName("parameterization", parameterization,
                "frequencies", new RealParameter("1.0"),
                "typeLabel", "type",
                "conditionOnSurvival", false,
                "tree", simulatedTree);

        double logProbTrue = bdmm.calculateLogP();

        System.out.println("Log probability true: " + logProbTrue);

        assertEquals(logProbEst, logProbTrue, 1e-1);
    }

    @Test
    public void typedSimpleLikelihoodTest() {
//        Randomizer.setSeed(53);
//        Randomizer.setSeed(42);
//        Randomizer.setSeed(4);
//        Randomizer.setSeed(1598647612262L);
//        Randomizer.setSeed(1598654582532L);
        System.out.println(Randomizer.getSeed());

        RealParameter frequencies = new RealParameter("0.1 0.1 0.1 0.1 0.1  0.1 0.1 0.1 0.1 0.1");
        int nTypes = 10;

        Parameterization parameterization = new CanonicalParameterization();
        parameterization.initByName(
                "typeSet", new TypeSet(nTypes),
                "origin", new RealParameter("5.0"),
                "finalSampleOffset", new RealParameter("0.0"), // To be set by simulation
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("2.0"), nTypes),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), nTypes),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.5"), nTypes),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), nTypes),
                "migrationRate", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.0"), nTypes));

        SimulatedTree simulatedTree = new SimulatedTree();
        simulatedTree.initByName(
                "parameterization", parameterization,
                "frequencies", frequencies,
                "minSamples", 2,
                "simulateUntypedTree", false);

        System.out.println(simulatedTree);
        System.out.println("Final sample offset: " + parameterization.getFinalSampleOffset());

        UntypedTreeFromTypedTree untypedSimulatedTree = new UntypedTreeFromTypedTree();
        untypedSimulatedTree.initByName(
                "typedTree", simulatedTree,
                "typeLabel", "type");
        System.out.println(untypedSimulatedTree);

        TypeMappedTree typeMappedTree = new TypeMappedTree();
        typeMappedTree.initByName(
                "parameterization", parameterization,
                "frequencies", frequencies,
                "typeLabel", "type",
                "untypedTree", untypedSimulatedTree);



        SampledTrajectory sampledTrajTrueTree = new SampledTrajectory();
        sampledTrajTrueTree.initByName(
                "typeMappedTree", simulatedTree,
                "parameterization", parameterization,
                "nParticles", 10000,
                "useTauLeaping", false,
                "stepsPerInterval", 5);

        System.out.println("Estimate from true typed tree:");
        System.out.println(sampledTrajTrueTree.getLogTreeProbEstimate());

        double [] logProbEsts = new double[100];
        double maxLogProbEst = Double.NEGATIVE_INFINITY;
        for (int i=0; i<logProbEsts.length; i++) {
            typeMappedTree.initAndValidate();

            SampledTrajectory sampledTrajectory = new SampledTrajectory();
            sampledTrajectory.initByName("typeMappedTree", typeMappedTree,
                    "parameterization", parameterization,
                    "nParticles", 1000,
                    "useTauLeaping", false,
//                    "resampThresh", 1.0,
                    "stepsPerInterval", 5);
            double thisEst = sampledTrajectory.getLogTreeProbEstimate() - Math.log(10);
            if (thisEst > maxLogProbEst)
                maxLogProbEst = thisEst;
            logProbEsts[i] = thisEst;
            System.out.println(thisEst);
        }

        double logProbEst = 0.0;
        for (int i=0; i<logProbEsts.length; i++)
            logProbEst += Math.exp(logProbEsts[i]-maxLogProbEst);
        logProbEst = Math.log(logProbEst/logProbEsts.length) + maxLogProbEst;

        System.out.println("Log probability estimate: " + logProbEst);

        BirthDeathMigrationDistribution bdmm = new BirthDeathMigrationDistribution();
        bdmm.initByName("parameterization", parameterization,
                "frequencies", frequencies,
                "typeLabel", "type",
                "conditionOnSurvival", false,
                "tree", untypedSimulatedTree);

        double logProbTrue = bdmm.calculateLogP();

        System.out.println("BDMM: Log probability true: " + logProbTrue);

        Parameterization param1type = new CanonicalParameterization();
        param1type.initByName(
                "typeSet", new TypeSet(1),
                "origin", new RealParameter("5.0"),
                "finalSampleOffset", new RealParameter(String.valueOf(parameterization.getFinalSampleOffset())),
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("2.0"), 1),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 1),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.5"), 1),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 1),
                "migrationRate", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.0"), 1));

        BirthDeathMigrationDistribution bdmm1type = new BirthDeathMigrationDistribution();
        bdmm1type.initByName("parameterization", param1type,
                "frequencies", new RealParameter("1.0"),
                "typeLabel", "type",
                "conditionOnSurvival", false,
                "useAnalyticalSingleTypeSolution", true,
                "tree", untypedSimulatedTree);

        double logProb1type = bdmm1type.calculateLogP();
        System.out.println("BDMM: Log probability 1 type: " + logProb1type);
//        System.out.println("BDMM: Log probability 1 type: " + (logProb1type - Math.log(2)));


//        assertEquals(logProbEst, logProbTrue, 1e-1);
    }

}