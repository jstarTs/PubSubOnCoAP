package Sink;

public class TaskReducerFactory {
	public static Object Create(String name) {
		Object object = null;
		if(name.trim().toLowerCase().equals("xmldogtaskreducer")) {
			object = new XMLDogTaskReducer();
		}
		else if(name.trim().toLowerCase().equals("saxontaskreducer")) {
			object = new SaxonTaskReducer();
		}
		else if(name.trim().toLowerCase().equals("yfiltertaskreducer")) {
			object = new YFilterTaskReducer();
		}
		
		return object;
	}
}
