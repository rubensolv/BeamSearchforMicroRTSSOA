package beam.model;

import java.util.ArrayList;

/**
 * 
 * @author rubens
 * Estes são os elementos (itens) que serão utilizados para compor o feixe.
 * Esta classe se parece com a classe Chromosome do projeto GA. 
 */

public class BeamElement {
	private ArrayList<Integer> elements;
	
	public BeamElement(){
		elements = new ArrayList<>();
	}

	public ArrayList<Integer> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Integer> elements) {
		this.elements = elements;
	}
	
	public void addElement(Integer element){
		this.elements.add(element);
	}
	
	public void print(){
		System.out.print("Beam Element ");
		for (Integer e : elements) {
			System.out.print(e+" ");
		}
		System.out.println("");
	}
	
	public void copyElements(ArrayList<Integer> elements){
		for (int integer : elements) {
			this.elements.add(integer);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeamElement other = (BeamElement) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}
}
