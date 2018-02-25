package beam.util.Evaluation;

import beam.model.Beam;

public interface RatePopulation {
	/* A função de avaliação irá controlar as chamadas no cluster, ou fazer os cálculos das simulações e entregar
	*  uma população devidamente avaliada.
	*  Lembrar que na população os cromossomos estão em um MAP onde 
	*  KEYS   = Cromossomo
	*  VALUES = Valor da avaliação 
	*/
	public Beam evalPopulation(Beam beam, int generation);
	public void finishProcess();
}
