import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.OWLObjectTransformer;

public class MainOwl {
	private static final String ONTOLOGY_PATH = "Ontology.owl";
	private static final String PREFIX = "http://www.ecology-and-endangered-species.com";
	
	static OWLOntology ontology;
	static OWLReasonerFactory rf = null;
	static OWLReasoner reasoner = null;
	static OWLOntologyManager manager;
	
	public static void main(String[] args) {
		// Create ontology manager
		manager = OWLManager.createOWLOntologyManager();
		
		// Load ontology
		System.out.println("\nLoading ontology...");
		File file = new File(ONTOLOGY_PATH);
		try {
			ontology = manager.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Ontology: " + ontology.getOntologyID());
        System.out.println("Format: " + manager.getOntologyFormat(ontology));
        System.out.println("Number of axioms: " + ontology.getLogicalAxiomCount());
        
        // Add 1 class and a SubclassOf axiom to the ontology
        System.out.println("\nAdding axioms to ontology...");
        // IRI prefix_IRI = IRI.create(PREFIX);
        OWLDataFactory df = manager.getOWLDataFactory();
        OWLClass threat = df.getOWLClass(PREFIX+"#Threat");
        OWLClass humanThreat = df.getOWLClass(PREFIX+"#HumanThreat");
        OWLSubClassOfAxiom human_sub_threat = df.getOWLSubClassOfAxiom(humanThreat, threat);
        ontology.add(human_sub_threat);
        
        // Comment the new axiom of the ontology
        System.out.println("\nAdding comment to new axioms...");
        OWLAnnotation comment = df.getOWLAnnotation(df.getRDFSComment(), 
        		df.getOWLLiteral("Class representing all threats caused by humans", "en"));
        OWLAxiom axiom = df.getOWLAnnotationAssertionAxiom(IRI.create(PREFIX+"#HumanThreat"), comment);
        manager.applyChange(new AddAxiom(ontology, axiom));
        
        // Check that the new axiom has been added to the ontology
        if(ontology.entitiesInSignature(IRI.create(PREFIX+"#HumanThreat")) != null)
        	System.out.println("\nHumanThreat class was correctly added to the ontology");
        System.out.println("Number of axioms: " + ontology.getLogicalAxiomCount());
        
        // Add 1 other class and an object property axiom to the ontology
        System.out.println("\nAdd a class 'Human'. Add the new axiom 'Human' 'SubClass Of' 'hasDiet some Omnivore'");
        OWLClass human = df.getOWLClass(PREFIX+"#Human");
        OWLClass omnivore = df.getOWLClass(PREFIX+"#Omnivore");
        OWLObjectProperty hasDiet = df.getOWLObjectProperty(PREFIX+"#hasDiet");
        OWLObjectSomeValuesFrom hasDietOmnivore = df.getOWLObjectSomeValuesFrom(hasDiet, omnivore);
        OWLSubClassOfAxiom humanHasDietOmnivore = df.getOWLSubClassOfAxiom(human, hasDietOmnivore);
        ontology.add(humanHasDietOmnivore);
        
        // Add 1 individual and an object property axiom to the ontology
        System.out.println("\nAdd an individual 'human'. Add the new axiom 'human' 'hasAnimalCharacteristic mammals'");
        OWLNamedIndividual human_individual = df.getOWLNamedIndividual(PREFIX+"#human");
        OWLObjectProperty hasAnimalCharacteristic = df.getOWLObjectProperty(PREFIX+"#hasAnimalCharacteristic");
        OWLNamedIndividual mammals_individual = df.getOWLNamedIndividual(PREFIX+"#mammals");
        OWLObjectPropertyAssertionAxiom humanHasAnimalCharacteristicMammals = df.getOWLObjectPropertyAssertionAxiom(hasAnimalCharacteristic, human_individual, mammals_individual);
        ontology.add(humanHasAnimalCharacteristicMammals);
        
        // Add a conflicting class Mutant, ie: a mix between a plant and an animal
        OWLClass mutant = df.getOWLClass(PREFIX+"#Mutant");
        OWLClass animalia = df.getOWLClass("https://en.wikipedia.org/wiki/Animalia");
        OWLClass plantae = df.getOWLClass("https://en.wikipedia.org/wiki/Plantae");
        OWLSubClassOfAxiom mutantAnimalia = df.getOWLSubClassOfAxiom(mutant, animalia);
        OWLSubClassOfAxiom mutantPlantae = df.getOWLSubClassOfAxiom(mutant, plantae);
        ontology.add(mutantAnimalia);
        ontology.add(mutantPlantae);
        
        
        // Changing an axiom in the ontology
        System.out.println("\nChanging axioms in ontology...");
        final Map<OWLClassExpression, OWLClassExpression> replacements = new HashMap<>();
        OWLClass tropical = df.getOWLClass(PREFIX + "#Tropical");
        OWLClass temperate = df.getOWLClass(PREFIX + "#Temperate");
        OWLObjectProperty hasClimate = df.getOWLObjectProperty(PREFIX + "#hasClimate");
        
        // Used to replace some axioms by others
    	// credits: http://syllabus.cs.manchester.ac.uk/pgt/2020/COMP62342/introduction-owl-api-msc.pdf
 		OWLObjectTransformer<OWLClassExpression> replacer = new OWLObjectTransformer<>(
 				(x) -> true, 
 				(oldValue) -> {
 					OWLClassExpression newValue = replacements.get(oldValue); 
 					if (newValue == null) {
 						return oldValue; 
 					}
 					return newValue;
 				}, 
 				df, 
 				OWLClassExpression.class
 		);
 		
        // Replace the occurrences of 'hasClimate some Tropical' by 'hasClimate some Temperate'
        replacements.put(df.getOWLObjectSomeValuesFrom(hasClimate, tropical), df.getOWLObjectSomeValuesFrom(hasClimate, temperate));
		List<OWLOntologyChange> results = replacer.change(ontology); 
		ontology.applyChanges(results);
		System.out.println("Number of axioms: " + ontology.getLogicalAxiomCount());
		
		// Remove an axiom from the ontology (Uncomment only if you want to remove the axiom 'HumanThreat SubClass Of Threat')
        //System.out.println("Removing axioms from ontology...");
        //OWLClass threat = df.getOWLClass(PREFIX+"#Threat");
        //OWLClass humanThreat = df.getOWLClass(PREFIX+"#HumanThreat");
        //OWLSubClassOfAxiom human_sub_threat = df.getOWLSubClassOfAxiom(humanThreat, threat);
        //ontology.remove(human_sub_threat);
        //System.out.println("Number of axioms: " + ontology.getLogicalAxiomCount());
        
        // Print all entities of the ontology
        //ontology.logicalAxioms().forEach(System.out::println);
		
        // Print all entities signatures starting with "H"
        System.out.println("\nAll entities starting with 'H':");
		ontology.signature().filter((e -> (!e.isBuiltIn()&&e.getIRI().getRemainder().orElse("").startsWith("H")))).forEach(System.out::println);
		// Note: HumanThreat is here !
		
		// Save the modified ontology in turtle format
        System.out.println("\nSaving ontology...");
        try {
			manager.saveOntology(ontology, new TurtleDocumentFormat(), new FileOutputStream(file));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
        // Load the reasoner
        System.out.println("\nLoading the reasoner...");
        rf = new ReasonerFactory();
        reasoner = rf.createReasoner(ontology);

        // Play with the reasoner
        System.out.println("\nSearching for the subclasses of 'Mammalia' (ie: mammals)");
        reasoner.getSubClasses(df.getOWLClass("https://en.wikipedia.org/wiki/Mammalia"), false).forEach(System.out::println);
        
        System.out.println("\nSearching for the domains of the object property 'hasClimate'");
        reasoner.getObjectPropertyDomains(df.getOWLObjectProperty(PREFIX + "#hasClimate"), false).forEach(System.out::println);
        
        System.out.println("\nSearching for the disjoint classes with the class 'Africa'");
        reasoner.getDisjointClasses(df.getOWLClass(PREFIX + "#Africa")).forEach(System.out::println);
        
        System.out.println("\nSuperclass of 'HumanThreat' (expected: 'Threat')");
        reasoner.getSuperClasses(humanThreat).forEach(System.out::println);
        
        System.out.println("\nInferred superclasses of 'Human' (expected: 'LivingOrganism'):");
        reasoner.getSuperClasses(human, false).forEach(System.out::println);
        
        System.out.println("\nInferred types of the individual 'human' (expected: 'Mammalia'):");
        reasoner.getTypes(human_individual).forEach(System.out::println);
        
        System.out.println("\nSearching for unsatisfiable classes (expected: 'Mutant')");
        reasoner.getUnsatisfiableClasses().forEach(System.out::println);
	}
}
