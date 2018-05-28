package principal;

import java.time.Instant;

import beam.model.Beam;
import beam.util.RunBeam;
import beam.util.Evaluation.RatePopulation;
import beam.util.Evaluation.RoundRobinEval;

public class RunTestes {
	
	public static void main(String[] args) {
		System.out.println("Tempo inicio "+Instant.now());
		//classe responsável por executar o beam search
		RunBeam beamSearch = new RunBeam();
		
		//função de avaliação (RR)
		RatePopulation fEval = new RoundRobinEval();
		
		Beam bFinal = beamSearch.run(fEval);
		System.out.println("Final Evaluation");
		bFinal.printWithValue();
		
		fEval.finishProcess();
		System.out.println("Tempo Fim "+Instant.now());
	}

}
