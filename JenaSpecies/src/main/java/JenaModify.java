import org.apache.jena.rdf.model.*;



import java.io.*;

public class JenaModify {

	public static void main(String[] args) throws IOException {
		
		/*
		 * This class demonstrates the basic management of an Ontology.
		 * The model is loaded, modified and saved.
		 * Individuals and their corresponding properties are added, gotten and removed
		 * @param args
	     * @throws IOException
		 */
		
		System.out.println("---------------Basic management of an Ontology---------------");

		String namespace = "Ontology.owl";
		String IRI = "http://www.ecology-and-endangered-species.com#";
		
		InputStream in;
		in = new FileInputStream(new File(namespace));
		
		// Create an empty in memory model (Load model)
		Model model = ModelFactory.createMemModelMaker().createModel("Model");
		model.read(in,null); // null base URI, since model URIs are absolute
		in.close();

		// Modify model
		// Add individuals (Seal and Fish)   
		Resource Seal = model.createResource(IRI + "Seal");
		
		// Add properties 
		Seal.addProperty(model.getProperty(IRI+"eats"), model.createResource(IRI+"Fish"));
		Seal.addProperty(model.getProperty(IRI+"hasConservationStatus"), "Vulnerable");
		Seal.addProperty(model.getProperty(IRI+"isEatenBy"), model.getResource(IRI+"WhiteShark"));		
		
		// Get individual, add and remove properties
		Resource WhiteShark = model.getResource(IRI + "WhiteShark");
		Seal.addProperty(model.getProperty(IRI+"isEatenBy"), model.getResource(IRI+"WhiteShark"));
		WhiteShark.removeAll(model.getProperty(IRI+"eats"));
		
		
		// Save model
		System.out.println("---------------SAVE CHANGES---------------");
		OutputStream out = new FileOutputStream(new File("JenaModifiedOntology.owl"));
		model.write(out, "RDF/XML");

		
		System.out.println("SAVED");
		out.close();

	}

}
