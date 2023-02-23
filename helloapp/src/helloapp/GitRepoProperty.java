package helloapp;

public class GitRepoProperty {
	public String 	strPathToRepo = "?",		//
							strPathToMdFile = "/",		//
							strRepoName = "repo name",	//
							strProjectName = "no name",				//
							strListOfMachine,			// 
							strRepoType;				// PCB, LIB, script, другое
	
	public GitRepoProperty(String strExtPathToRepo)
	{
		this.strPathToRepo = strExtPathToRepo;
		this.strPathToMdFile = "./";
		this.strRepoName = "repo name";
		this.strProjectName = "project name";
		this.strListOfMachine = "list of machine";
		this.strRepoType = "type of repo";
		
		//System.out.println( "create GitRepoProperty" );
	}
	
}
