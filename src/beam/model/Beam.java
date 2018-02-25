package beam.model;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 
 * @author rubens
 * Classe que irá conter os elementos que serão testados em cada nível da busca.
 * Esta classe se parece com a classe Population do projeto GA.
 */
public class Beam {
	
	private HashMap<BeamElement, BigDecimal> beam;
	
	public Beam(){
		setBeam(new HashMap<>());
	}
	
	public Beam(HashMap<BeamElement, BigDecimal> b){
		this.beam = b;
	}

	public HashMap<BeamElement, BigDecimal> getBeam() {
		return beam;
	}

	public void setBeam(HashMap<BeamElement, BigDecimal> beam) {
		this.beam = beam;
	}
	
	public void addBeamElement(BeamElement bEl){
		this.beam.put(bEl, BigDecimal.ZERO);
	}
	
	public void print(){
		System.out.println("-- Beam --");
		for(BeamElement b : beam.keySet()){
			b.print();
		}
		System.out.println("-- Beam --");
	}
	
	public void printWithValue(){
		System.out.println("-- Beam --");
		for(BeamElement c : beam.keySet()){
			c.print();
			System.out.println("Value = "+ this.beam.get(c));
		}
		System.out.println("-- Beam --");
	}
	
	/**
	 * Função que zera os valores das avaliações dos BeamElement.
	 */
	public void clearValueBeamElement(){
		for(BeamElement chromo : this.beam.keySet()){
			this.beam.put(chromo, BigDecimal.ZERO);
		}
	}
	
	//static methods
	/**
	 * Cria um beam com os elementos (indices) dos scripts que serão utilizados
	 * @param size quantitativo máximo de elementos do beam
	 * @return um beam com o key = BeamElement e Values = 0
	 */
	public static Beam getInitialBeam(int size){
		HashMap<BeamElement, BigDecimal> tBeam = new HashMap<>();
		
		BeamElement bEl;
		for (int i = 0; i < size; i++) {
			bEl = new BeamElement();
			bEl.addElement(i);
			
			tBeam.put(bEl, BigDecimal.ZERO);
		}
		
		
		
		Beam b = new Beam(tBeam);
		return b;
	}
	
	
}
