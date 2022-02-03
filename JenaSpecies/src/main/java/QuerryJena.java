import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import java.io.*;


public class QuerryJena {

	/**
	 * A JENA API is modeled for and Ontology with the subject Ecology
	 * This class allows to load different SPARQL queries
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {

		System.out.println("---------------JENA API FOR ECOLOGY ONTOLOGY---------------");
		System.out.println();
		System.out.println("---------------SHOWING AVAILABLE QUERIES-------------------");
		System.out.println();
		
		System.out.println(	"QUERY 1 - List the existing animal threats and where they occur and count how many species are threatened by it in this location.");
		BufferedReader br = new BufferedReader(new FileReader("query1.qt"));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();

		String stringQuery = sb.toString();

		// REPEAT FOR SHOW ALL THE QUERIES AVAILABLES, CHANGE THE NAME OF THE QUERIES

		System.out.println("QUERY 2 - Find the classification of the Mollusca class. Lists its direct subclasses and give the property and the value to give to classify an animal in this subclass.");
		BufferedReader br2 = new BufferedReader(new FileReader("query2.qt"));
		StringBuilder sb2 = new StringBuilder();
		String line2 = br2.readLine();

		while (line2 != null) {
			sb2.append(line2);
			sb2.append(System.lineSeparator());
			line2 = br2.readLine();
		}
		br2.close();

		String stringQuery2 = sb2.toString();

		System.out.println("QUERY 3 - Find the location and threats of plants who have conductor vessels and produce seeds.");
		BufferedReader br3 = new BufferedReader(new FileReader("query3.qt"));
		StringBuilder sb3 = new StringBuilder();
		String line3 = br3.readLine();

		while (line3 != null) {
			sb3.append(line3);
			sb3.append(System.lineSeparator());
			line3 = br3.readLine();
		}
		br3.close();

		String stringQuery3 = sb3.toString();

		System.out.println("QUERY 4 - List the species and their conservation status.");
		BufferedReader br4 = new BufferedReader(new FileReader("query4.qt"));
		StringBuilder sb4 = new StringBuilder();
		String line4 = br4.readLine();

		while (line4 != null) {
			sb4.append(line4);
			sb4.append(System.lineSeparator());
			line4 = br4.readLine();
		}
		br4.close();

		String stringQuery4 = sb4.toString();

		System.out.println("QUERY 5 - List all the properties that defines the Eurasian otter.");
		BufferedReader br5 = new BufferedReader(new FileReader("query5.qt"));
		StringBuilder sb5 = new StringBuilder();
		String line5 = br5.readLine();

		while (line5 != null) {
			sb5.append(line5);
			sb5.append(System.lineSeparator());
			line5 = br5.readLine();
		}
		br5.close();

		String stringQuery5 = sb5.toString();


		// Create the model for our inferences
		String namespace = "Ontology.owl";
		
		InputStream in;
		in = new FileInputStream(new File(namespace));
		Model model = ModelFactory.createMemModelMaker().createModel(null);
		model.read(in, null);
		in.close();

		// Select a query to load
		System.out.println();
		System.out.println("---------------SELECT A QUERY TO LOAD---------------");
		
		boolean load = true;
		
		while (load == true) {

			System.out.println("Type 1, 2, 3, 4 or 5 for chose a query to load: ");
			
			int querynumber = Read.readInt();
			System.out.println();
			Query query = null;

			if (querynumber == 1) {

				System.out.println(stringQuery);
				query = QueryFactory.create(stringQuery);

			} else if (querynumber == 2) {

				System.out.println(stringQuery2);
				query = QueryFactory.create(stringQuery2);


			} else if (querynumber == 3) {
				
				System.out.println(stringQuery3);
				query = QueryFactory.create(stringQuery3);


			} else if (querynumber == 4) {
				
				System.out.println(stringQuery4);
				query = QueryFactory.create(stringQuery4);

			} else if (querynumber == 5) {

				System.out.println(stringQuery5);
				query = QueryFactory.create(stringQuery5);

			} else {
				
				System.out.println("Error, chose a valid number");
				continue;
			}
			
			
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();
			ResultSetFormatter.out(System.out, results, query);
			qe.close();
			
			System.out.println("If you wish to load another query type 1:");
			int choice = Read.readInt();
			System.out.println();
			if (choice == 1) {
				System.out.println("QUERY 1 - List the existing animal threats and where they occur and count how many species are threatened by it in this location.");
				System.out.println("QUERY 2 - Find the classification of the Mollusca class. Lists its direct subclasses and give the property and the value to give to classify an animal in this subclass.");
				System.out.println("QUERY 3 - Find the location and threats of plants who have conductor vessels and produce seeds.");
				System.out.println("QUERY 4 - List the species and their conservation status.");
				System.out.println("QUERY 5 - List all the properties that defines the Eurasian otter.");
				continue;
			} else {
				load = false;
			}

		}

	}
}