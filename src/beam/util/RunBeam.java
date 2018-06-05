package beam.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import beam.config.ConfigBeam;
import beam.model.Beam;
import beam.model.BeamElement;
import beam.util.Evaluation.RatePopulation;

public class RunBeam {
	
	private Beam beam;
	private int deep;
	private Set<BeamElement> initialElements;
	private boolean continueEval;
	private ArrayList<BeamElement> fathers;
	private ArrayList<BeamElement> finalFathers = new ArrayList<>();
	
	public Beam run(RatePopulation evalFunction){
		// gerar o feixe com os scripts individuais
		beam = Beam.getInitialBeam(ConfigBeam.QTD_SCRIPTS);
		this.initialElements = beam.getBeam().keySet();

		//inicio os controladores
		resetControls();
		
		//critério de parada
		while(continueProcess()){
			//Aplico a avaliação da população 
			beam = evalFunction.evalPopulation(beam, deep);
			
			//log
			System.out.println("Eval do feixe na profundidade "+ deep);
			beam.printWithValue();
			
			//seleciono os melhores K indivíduos e já monto o novo beam
			beam = buildNewBeam(beam);	
			
			//log
			System.out.println("Novo feixe criado");
			beam.printWithValue();
			
			//atualiza a profundidade
			updateDeep();
		}
		
		return evalFunction.evalPopulation(beam, deep);
	}

	
	/**
	 * Função que visa selecionar os K melhores elementos do feixe e compor um novo feixe utilizando
	 * os filhos dos k melhores elementos.
	 * @param bTemp Feixe com os elementos e seus valores atualizados
	 * @return um novo feixe (de valores zerados) filhos do k melhores elementos do feixe passado como parametro
	 */
	private Beam buildNewBeam(Beam bTemp) {
		//capturo os melhores 
		HashMap<BeamElement, BigDecimal> newBeam = (HashMap<BeamElement, BigDecimal>)sortByValue(bTemp.getBeam());
		
		//valido se algum pai foi eleito como melhor beam
		validateFatherIncrease(newBeam);
		
		//log
		System.out.println("Melhores" + ConfigBeam.QTD_K +"elementos");
		System.out.println(newBeam);
		//keep the best father
		finalFathers.addAll(newBeam.keySet());
		
		//crio todos os filhos baseados no número K de pais
		HashMap<BeamElement, BigDecimal> fullBeam = buildFullBeam(newBeam.keySet());
		
		//incluo os pais no proximo nível 
		for(BeamElement bFather : newBeam.keySet()){
			fullBeam.put(bFather, BigDecimal.ZERO);
		}
		//insiro todos os pais se for o ultimo nível
		if(ConfigBeam.MAX_DEEP == this.deep){
			for(BeamElement bFather : finalFathers){
				fullBeam.put(bFather, BigDecimal.ZERO);
			}
		}
		
		Beam bRet = new Beam(fullBeam);
		return bRet;
	}
	
	/**
	 * Recebe os melhores elementos do feixe e verifica se, dentre eles, existe algum dos pais. 
	 * Caso exista, o processo deve ser parado. Caso contrário segue normal.
	 * Se a  variavel fathers estiver vazia, é a primeira iteração e não deve ser avaliado.
	 * @param newBeam
	 */
	private void validateFatherIncrease(HashMap<BeamElement, BigDecimal> newBeam) {
		if(fathers.size() == 0){
			fathers.addAll(newBeam.keySet());
		}else{
			//realizo a avaliação
			for(BeamElement f : fathers){
				if(newBeam.containsKey(f)){
					System.out.println("Pai e um dos melhores. Parar processo!");
					//this.continueEval = false;
				}
			}
		}
		//se nenhum dos novos filhos contiver um dos pais o processo irá continuar
		//atualizo a listagem de pais
		if(this.continueEval == true){
			fathers.clear();
			fathers.addAll(newBeam.keySet());
		}
		
	}


	/**
	 * Função que cria um novo feixe baseado nos elementos passados como parametro
	 * @param newBeam k melhores elementos do feixe
	 * @return um novo feixe com os filhos dos k melhores elementos passados
	 */
	private HashMap<BeamElement, BigDecimal> buildFullBeam(Set<BeamElement> newBeam) {
		HashMap<BeamElement, BigDecimal> childBeam = new HashMap<>();
		
		//log
		System.out.println("Filhos criados ");
		
		for (BeamElement beamElement : newBeam) {
			//compoe os filhos de cada elemento 
			for(BeamElement bchild : buildChildren(beamElement)){
				//log
				bchild.print();
				
				childBeam.put(bchild, BigDecimal.ZERO);
			}
		}
		
		return childBeam;
	}


	/**
	 * Compõe todos os filhos de um elemento passado
	 * @param beamElement no pai que sera utilizado para compor seus filhos
	 * @return arrayList de filhos
	 */
	private ArrayList<BeamElement> buildChildren(BeamElement bFather) {
		ArrayList<Integer> indices = getIndices(this.initialElements);
		//remove dos indices iniciais os já existentes no indice pai
		for(Integer i : bFather.getElements()){
			indices.remove(i);
		}
		//construo os filhos
		ArrayList<BeamElement> child = new ArrayList<>();
		BeamElement bTemp;
		for(Integer i : indices){
			bTemp = new BeamElement();
			bTemp.copyElements(bFather.getElements());
			bTemp.addElement(i);
			child.add(bTemp);
		}
		
		return child;
	}


	private ArrayList<Integer> getIndices(Set<BeamElement> initialElements2) {
		HashSet<Integer> indices = new HashSet<>();
		
		for (BeamElement b : initialElements2) {
			indices.addAll(b.getElements());
		}
		return new ArrayList<>(indices);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List list = new LinkedList(map.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
			if(sortedHashMap.size()==ConfigBeam.QTD_K)
			{
				break;
			}
		} 
		return sortedHashMap;

	}


	private void updateDeep(){
		this.deep++;
	}
	
	
	
	private boolean continueProcess() {
		/*if(continueEval == false){
			return false;
		}*/
		if(deep <= ConfigBeam.MAX_DEEP ){
			return true;
		}
		return false;
	}
	
	protected void resetControls(){
		this.deep = 0;
		this.continueEval = true;
		this.fathers = new ArrayList<>();
	}

}
