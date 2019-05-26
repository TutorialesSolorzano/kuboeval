package mx.kubo.eval.main;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import mx.kubo.eval.model.Busqueda;
import mx.kubo.eval.model.Persona;

public class KuboEvalRun {

	public static void main(String[] args) {

		KuboEvalRun kuboEvalRun= new KuboEvalRun();
		switch(args[0]) {
		  case "add":
			  if(args[1]!=null) {
			
					try {
						Persona p=(Persona)kuboEvalRun.fromJson(kuboEvalRun.concatArray(args), Persona.class);
						kuboEvalRun.writeObject(p);
						System.out.println("Usuario agregado");
					} catch (JsonParseException e) {
						System.out.println("JsonParseException");
						e.printStackTrace();
					} catch (JsonMappingException e) {
						System.out.println("JsonMappingException");
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("IOException");
						e.printStackTrace();
					}
		
			  }
		    break;
		  case "list":
			  try {
				String jsonResult=kuboEvalRun.readObjects();
				System.out.println(jsonResult);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    break;
		  case "fuzzy-search":
			  if(args[1]!=null) {
					
					try {
						Busqueda b=(Busqueda)kuboEvalRun.fromJson(kuboEvalRun.concatArray(args), Busqueda.class);
						Persona p=kuboEvalRun.search(b.getSearch().replaceAll("%", ""), kuboEvalRun.readObjectsToList());
			            if(p!=null) {
			            	String pJson=new ObjectMapper().writeValueAsString(p);
			        		System.out.println(pJson);
			            }else {
			            	System.out.println("Sin coincidencias");
			            }
					} catch (JsonParseException e) {
						System.out.println("JsonParseException");
						e.printStackTrace();
					} catch (JsonMappingException e) {
						System.out.println("JsonMappingException");
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("IOException");
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
			  }
		    break;
		    
		  default:
		    System.out.println("Accion incorrecta");
		}

	}

	public <T> Object fromJson(String json, Class<T> clase) throws JsonParseException, JsonMappingException, IOException {	
		T o = new ObjectMapper().readValue(json, clase);

		return o;
	}
	
	public String concatArray(String args[]) {
		String result="";
		for(int i=1; i<args.length; i++) {
			result+=args[i]+"%";
		}
		return result;
	}
	
    public void writeObject(Persona p) throws IOException {
Persona pe= new Persona();
pe.setName(p.getName());
    	FileOutputStream f = new FileOutputStream(new File("fuzzy-search.txt"), true);
		ObjectOutputStream o = new ObjectOutputStream(f);

		o.writeObject(pe);

		o.close();
		f.close();
    }
    
    public String readObjects() throws ClassNotFoundException, IOException{
    	List<Persona> personas=new ArrayList<>();
    	try {
    	FileInputStream fi = new FileInputStream(new File("fuzzy-search.txt"));
		//ObjectInputStream oi = new ObjectInputStream(fi);

		boolean flagBucle=true;
		
		while(flagBucle){
			ObjectInputStream oi = new ObjectInputStream(fi);
			Persona p = (Persona) oi.readObject();
			p.setName(p.getName().replace('%', ' ').trim());			
		      if(p != null) {
		         personas.add(p);
		      }
		      else {
		    	  flagBucle = false;
		      }
		      //oi.close();
		   }

		//oi.close();
		fi.close();
    	}catch(EOFException ex) {    		
    	}
    	
		
    	List<Persona> sortedPersonas=personas.stream().sorted((object1, object2) -> object1.getName().compareTo(object2.getName())).collect(Collectors.toList());;
    	
		String jsonInString = new ObjectMapper().writeValueAsString(sortedPersonas);
		
		return jsonInString;
    }
    
    
    public Persona search(String busqueda, List<Persona> personas) {
    	Persona result=null;
    	
    	int tam=busqueda.length();
    	String fragm="";
    	
    	int coincidencias=0;
    	int inicioFragm=0;
    	
    	String fragmAux="";
    	
    	for(int i=0; i<tam; i++) {
    		fragm= busqueda.substring(inicioFragm, i+(i+1 > tam ? 0 : 1));
    		coincidencias=0;
    		for(int ii=0; ii<personas.size(); ii++) {
    			if(personas.get(ii).getName().toUpperCase().contains(fragm.toUpperCase())) {
    				personas.get(ii).setCoincidences(personas.get(ii).getCoincidences()+1);
    				coincidencias++;
    			}
    		}
    		
    		if(coincidencias==0 && !fragm.equals(fragmAux)) {
    			fragmAux=fragm;
    			i--;
    			inicioFragm=i+1;
    		}
    		             		
    	}
    	
    	
    	
    	Optional<Persona> oPer= personas.stream().filter(x -> x.getCoincidences()>0) 
						.max(Comparator.comparingInt(Persona::getCoincidences));

    	if(oPer.isPresent()) {
    		result=oPer.get();
    	}
    	
    	
    	return result;
    }
    
    
    public List<Persona> readObjectsToList() throws ClassNotFoundException, IOException{
    	List<Persona> personas=new ArrayList<>();
    	try {
    	FileInputStream fi = new FileInputStream(new File("fuzzy-search.txt"));
		//ObjectInputStream oi = new ObjectInputStream(fi);

		boolean flagBucle=true;
		
		while(flagBucle){
			ObjectInputStream oi = new ObjectInputStream(fi);
			Persona p = (Persona) oi.readObject();
			p.setName(p.getName().replace('%', ' ').trim());
			System.out.println(p.toString());
		      if(p != null) {
		         personas.add(p);
		      }
		      else {
		    	  flagBucle = false;
		      }
		      //oi.close();
		   }

		//oi.close();
		fi.close();
    	}catch(EOFException ex) {
    		System.out.println("eofe");
    	}

		return personas;
    }

}
