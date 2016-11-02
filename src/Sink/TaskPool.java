package Sink;

import java.util.ArrayList;
import java.util.List;

public class TaskPool 
{
	public static List<XMLDogTask> CreateXMLDogTasks(int size, List<String> xpathList) {
		List<XMLDogTask> list = new ArrayList<XMLDogTask>();
		for(int i = 0; i < size; ++i) {
			list.add((XMLDogTask) FilterTaskFactory.Create("xmldog", xpathList));
		}
		return list;
	}
	
	public static List<SaxonTask> CreateSaxonTasks(int size, List<String> xpathList) {
		List<SaxonTask> list = new ArrayList<SaxonTask>();
		for(int i = 0; i < size; ++i) {
			list.add((SaxonTask) FilterTaskFactory.Create("saxon", xpathList));
		}
		return list;
	}
	
	public static List<YFilterTask> CreateYFilterTasks(int size, List<String> xpathList) {
		List<YFilterTask> list = new ArrayList<YFilterTask>();
		for(int i = 0; i < size; ++i) {
			list.add((YFilterTask) FilterTaskFactory.Create("yfilter", xpathList));
		}
		return list;
	}
	
	public static List<YFilterTask> CreateImprovedYFilterTasks(int size, List<String> xpathList) {
		List<YFilterTask> list = new ArrayList<YFilterTask>();
		for(int i = 0; i < size; ++i) {
			list.add((YFilterTask) FilterTaskFactory.Create("improvedyfilter", xpathList));
		}
		return list;
	}
	
}
