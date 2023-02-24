package helloapp;

public class GitRepoProperty {
	public String 	strPathToRepo = "?",		//
					strPathToMdFile = "/",		//
					strRepoName = "repo name",	//
					strProjectName = "no name",				//
					strListOfMachine,			// 
					strRepoType;				// PCB, LIB, SCRIPTS, другое
	
	public GitRepoProperty()
	{
		this.strPathToRepo = "?";
		this.strPathToMdFile = "./";
		this.strRepoName = "repo name";
		this.strProjectName = "no project name";
		this.strListOfMachine = "list of machine is empty";
		this.strRepoType = "type is not define";
		
		//System.out.println( "create GitRepoProperty" );
	}
	
	public GitRepoProperty(String strExtPathToRepo)
	{
		this.strPathToRepo = strExtPathToRepo;
		this.strPathToMdFile = "./";
		this.strRepoName = "repo name";
		this.strProjectName = "no project name";
		this.strListOfMachine = "list of machine is empty";
		this.strRepoType = "type is not define";
		
		//System.out.println( "create GitRepoProperty" );
	}
	
}
