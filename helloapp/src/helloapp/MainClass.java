package helloapp;

//import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
//import java.util.HashSet;
import java.util.ArrayList;
import java.io.*;

public class MainClass 
{
	private static GitRepoProperty[] gitList;
	
	private static String	strKeyGit = "-git",
							strKeyMaster = "-master",
							strKeyHelp = "-h",
							strGitPath = "./",
							strMasterPath = "./",
							strGitSearchMask = ".git",
							strGitListFileName = "git-list.html",
							strMdFileMask = "/README.md";
							
	private static int intCommand = 0;	// 0 - нет действий
										// 1 - поиск папок .git и генерация 'git-list.txt'
										// 2 - по файлу 'git-list.txt' анализ .md-файлов и генерация 'repo-list.html'
	
	public static void main(String[] args) throws IOException
	{
		ArrayList<String> astrListOfPath = new ArrayList<String>();
		
		checkArguments( args );
		
		System.out.println( "<GitPath>:" );
		System.out.println( strGitPath );
		System.out.println( "<MasterPath>:" );
		System.out.println( strMasterPath );
		System.out.print( "Command code:" );
		//System.out.println( intCommand );
		
		switch (intCommand)
		{
			default:
			{
				System.out.println( intCommand + ": No action" );
				break;
			}
			case 1:
			{
				System.out.println( intCommand + ": Parcing git-repository path..." );
				astrListOfPath = makeListOfPath( strGitPath );
				
				System.out.println( "saving to: " + strGitPath + strGitListFileName);
				FileWriter fileGitList = new FileWriter( strGitPath + strGitListFileName, false );	//создание файла для сохранения
				
				fileGitList.write( "<html>\n" );	// генерация и заполнение HTML
				fileGitList.write( "<body>\n" );
				fileGitList.write( "<p><a href='http://git2061'>Return to parent directory</a></p>\n" );
				for (int i=0; i<astrListOfPath.size(); i++)
				{
					fileGitList.write( "<p>" );
					fileGitList.write( "git@git2061:" );
					fileGitList.write( astrListOfPath.get(i) );
					fileGitList.write( "</p>" );
					fileGitList.append( "\n" );
				}
				fileGitList.write( "</body>\n" );
				fileGitList.write( "</html>\n" );
				fileGitList.close();
				System.out.println( "file is saved" );	// закрытие файла после окончания записи
				break;
			}
			case 2:
			{
				System.out.println( intCommand + ": Parcing git-repository path..." );
				astrListOfPath = makeListOfPath( strGitPath );
				
				//gitList = new GitRepoProperty[ astrListOfPath.size() ];
				gitList = makeListOfGitProperty( astrListOfPath, strMasterPath );
				
				for (int i=0; i<astrListOfPath.size(); i++)
				{
					try 
					{
						BufferedReader fileMD = new BufferedReader( new FileReader( gitList[i].strPathToMdFile ) );
						//FileReader fileMD = new FileReader( gitList[i].strPathToMdFile );
						//System.out.println( "file: " + gitList[i].strPathToMdFile + " is found" );
						String bufString = fileMD.readLine();
						while ( bufString != null )
						{
							gitList[i] = checkStringFromMdFile( gitList[i], bufString );	// тут не очень оптимальное место, потом мб переработать
							//System.out.println( bufString );
							bufString = fileMD.readLine();
						}
						fileMD.close();
					}	
					catch (FileNotFoundException e)
					{
						System.out.println( "file: " + gitList[i].strPathToMdFile + " not found" );
					}
					
				}
				
				
				System.out.println( "saving to: " + strGitPath + strGitListFileName);
				FileWriter fileGitList = new FileWriter( strGitPath + strGitListFileName, false );	//создание файла для сохранения
				
				fileGitList.write( "<html>\n" );	// генерация и заполнение HTML
				fileGitList.write( "<body>\n" );
				fileGitList.write( "<p><a href='http://git2061'>Return to parent directory</a></p>\n" );
				
				fileGitList.write( "<table border=\"1\">\n" );
				fileGitList.write( "<tr>\n"
									  + "	<td>Path to repo</td>\n"
									  + "	<td>Repo name</td>\n "
									  + "	<td>Project name</td>\n "
									  + "	<td>List of machines</td>\n "
									  + "	<td>Repo type</td> </tr>\n" );
				
				for (int i=0; i<astrListOfPath.size(); i++)
				{
					fileGitList.write( "<tr>\n" );
					fileGitList.write( "	<td>git@git2061:" + gitList[i].strPathToRepo + "</td>\n" );
					
					fileGitList.write( "	<td>" + gitList[i].strRepoName + "</td>\n" );
					
					fileGitList.write( "	<td>" + gitList[i].strProjectName + "</td>\n" );
					fileGitList.write( "	<td>" + gitList[i].strListOfMachine + "</td>\n" );
					fileGitList.write( "	<td>" + gitList[i].strRepoType + "</td>\n" );
					fileGitList.write( "</tr>\n" );
					//fileGitList.write( gitList[i].strPathToMdFile + ";" );
					/*
					fileGitList.write( gitList[i].strListOfMachine + ";" );
					fileGitList.write( gitList[i].strRepoType + ";" );
					fileGitList.append( "\n" );
					*/
				}
				fileGitList.write( "</table>\n" );
				
				fileGitList.write( "</body>\n" );
				fileGitList.write( "</html>\n" );
				fileGitList.close();
				System.out.println( "file is saved" );	// закрытие файла после окончания записи
				break;
				// поля: индексы агрегатов; 
			}
		}
		
    }
	
	private static GitRepoProperty checkStringFromMdFile( GitRepoProperty inputRepoProperty, String strFromMdFile )
	{
		String 	strProjectNameMsk = "ProjectName:",
				strListOfMachineMask = "ListOfMachine:",
				strRepoTypeMask = "RepoType:";
		
		if ( strFromMdFile.indexOf( strProjectNameMsk ) >= 0 )												// в строке содержится имя проекта?
		{
			inputRepoProperty.strProjectName = strFromMdFile.substring( strProjectNameMsk.length() );
		}
		
		if ( strFromMdFile.indexOf( strListOfMachineMask ) >= 0 )											// в строке содержатся индексы агрегатов?
		{
			inputRepoProperty.strListOfMachine = strFromMdFile.substring( strListOfMachineMask.length() );
		}
		
		if ( strFromMdFile.indexOf( strRepoTypeMask ) >= 0 )												// в строке содержится тип репозитория?
		{
			inputRepoProperty.strRepoType = strFromMdFile.substring( strRepoTypeMask.length() );
		}
		
		return inputRepoProperty;
	}
	
	private static GitRepoProperty[] makeListOfGitProperty( ArrayList<String> astrList, String strPathToGitMaster )
	{
		int intArrSize = astrList.size();
		GitRepoProperty[] gitBufList = new GitRepoProperty[ intArrSize ];
		
		for (int i=0; i<intArrSize; i++)
		{
			gitBufList[i] = new GitRepoProperty( astrList.get(i) );
			//System.out.println( "array size: " + gitBufList.length );
			String bufString = astrList.get(i).substring( strGitPath.length() );
			gitBufList[i].strRepoName = bufString.substring(0, bufString.length() - strGitSearchMask.length() );
			gitBufList[i].strPathToMdFile = strPathToGitMaster + gitBufList[i].strRepoName + strMdFileMask;
		}
		
		return gitBufList;
	}
	
	private static void checkArguments(String[] strArgs)
	{
		boolean boolAnyErrArguments = true;
		
		if ( ( strArgs.length == 1 ) && ( strArgs[0].equalsIgnoreCase(strKeyHelp) ) ) 	// проверка, что единственный ключ -h
		{
			boolAnyErrArguments = false;
			intCommand = 0;
			
			System.out.println( "use '-h' for help. Must be only key." );
			System.out.println( "use '-git' for choose <GitPath>, '-master' for choose path when <MasterPath> branch was cloned" );
			System.out.println( "examples for '-git' and '-master':" );
			System.out.println( "example 1: command '-git <GitPath>' is parcing <GitPath> to fing '.git' folders and generate 'git-list.html' in <GitPath>" );
			System.out.println( "example 2: command '-master <MasterPath> -git <GitPath>' or '-git <GitPath> -master <MasterPath>' " );
			System.out.println( "this command use <GitPath> to parcing .md-files in master-repo in <MasterPath> and generate html-guide 'repo-list.html' in <MasterPath>." );
		}
		
		if ( ( strArgs.length == 2 ) && ( strArgs[0].equalsIgnoreCase(strKeyGit) ) ) 	// проверка, что два аргумента и первый ключ -git
		{
			boolAnyErrArguments = false;
			intCommand = 1;
			
			strGitPath = strArgs[1];
			char charLastSymbol = strGitPath.charAt( strGitPath.length()-1 );
			//System.out.println( charLastSymbol );
			if ( (charLastSymbol != '/') )
			{
				strGitPath = strGitPath + "/";	// проверка на то, что путь заканчивается символом '/'
			}
		}
		
		if ( ( strArgs.length == 4 ) && ( strArgs[0].equalsIgnoreCase(strKeyGit) ) && ( strArgs[2].equalsIgnoreCase(strKeyMaster) ) )
			// проверка, что четыре аргумента, первый ключ -git, а третий -master
		{
			boolAnyErrArguments = false;
			intCommand = 2;
			
			strGitPath = strArgs[1];
			char charLastSymbol = strGitPath.charAt( strGitPath.length()-1 );
			if ( (charLastSymbol != '/') )
			{
				strGitPath = strGitPath + "/";	// проверка на то, что путь заканчивается символом '/'
			}
			
			strMasterPath = strArgs[3];
			charLastSymbol = strMasterPath.charAt( strMasterPath.length()-1 );
			if ( (charLastSymbol != '/') )
			{
				strMasterPath = strMasterPath + "/";	// проверка на то, что путь заканчивается символом '/'
			}
			
		}
		
		if ( boolAnyErrArguments == true )	// проверка на любые другие незадекларированные комбинации ключей и аргументов
		{
			intCommand = 0;
			
			System.out.println( "ERROR: incorrect keys or arguments, use '-h' for HELP" );
		}

	}
	
	private static ArrayList<String> makeListOfPath(String pathString) throws IOException
	{
		//String pathString = "/Users/anton/eclipse-workspace/helloapp";
		ArrayList<String> arrayList = new ArrayList<>();
		
		Files.walkFileTree(Paths.get(pathString)/*,new HashSet<>(), 10*/, new FileVisitor<Path>() 
	        {
	            @Override
	            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
	                    throws IOException 
	            {
	                //System.out.println("preVisitDirectory: " + dir);
	                return FileVisitResult.CONTINUE;
	            }
	
	            @Override
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	                    throws IOException 
	            {
	                //System.out.println("visitFile: " + file);
	                return FileVisitResult.CONTINUE;
	            }
	
	            @Override
	            public FileVisitResult visitFileFailed(Path file, IOException exc)
	                    throws IOException 
	            {
	                //System.out.println("visitFileFailed: " + file);
	                return FileVisitResult.CONTINUE;
	            }
	
	            @Override
	            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
	                    throws IOException 
	            {
	            	String strBuf = dir.toString();
	            	if ( ( strBuf.indexOf(strGitSearchMask)>0 ) && 
	            		 ( strBuf.indexOf(strGitSearchMask)==( strBuf.length()-strGitSearchMask.length() ) ) )	
	            		// поиск подстроки .git в строках путей и подстрока .git должна быть в конце
	            	{
	            		arrayList.add( strBuf );
	            		//System.out.println("postVisitDirectory: " + strBuf);
	            	}
	                
	                return FileVisitResult.CONTINUE;
	            }
	        });
		
		return arrayList;
	}
	
}
