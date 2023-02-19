package helloapp;

//import java.util.Scanner;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
//import java.util.HashSet;
import java.util.ArrayList;
import java.io.*;

public class MainClass 
{
	private static String	strKeyGit = "-git",
							strKeyMaster = "-master",
							strKeyHelp = "-h",
							strGitPath = "./",
							strMasterPath = "./",
							strGitSearchMask = ".git",
							strGitListFileName = "git-list.txt";
							
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
		System.out.println( "Command code:" );
		System.out.println( intCommand );
		
		switch (intCommand)
		{
			default:
			{
				System.out.println( "no action" );
				break;
			}
			case 1:
			{
				System.out.println( "parcing git-repository path..." );
				astrListOfPath = makeListOfPath( strGitPath );
				
				System.out.println( "saving to: " + strGitPath + strGitListFileName);
				FileWriter fileGitList = new FileWriter( strGitPath + strGitListFileName, false );
				for (int i=0; i<astrListOfPath.size(); i++)
				{
					fileGitList.write( astrListOfPath.get(i) );
					fileGitList.append( "\n" );
				}
				fileGitList.close();
				System.out.println( "file is saved" );
				//strGitListFileName
				//System.out.println( "arrayList length: " + astrListOfPath.size() );
				//System.out.println( astrListOfPath );
				break;
			}
			case 2:
			{
				System.out.println( "case 2" );
				break;
			}
		}
		
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
			System.out.println( "example 1: command '-git <GitPath>' is parcing <GitPath> to fing '.git' folders and generate 'git-list.txt' in <GitPath>" );
			System.out.println( "example 2: command '-master <MasterPath> -git <GitPath>' or '-git <GitPath> -master <MasterPath>' " );
			System.out.println( "this command use 'git-list.txt' in <GitPath> to parcing .md-files in master-repo in <MasterPath> and generate html-guide 'repo-list.html' in <MasterPath>." );
		}
		
		if ( ( strArgs.length == 2 ) && ( strArgs[0].equalsIgnoreCase(strKeyGit) ) ) 	// проверка, что два аргумента и первый ключ -git
		{
			boolAnyErrArguments = false;
			intCommand = 1;
			
			strGitPath = strArgs[1];
			// проверка не то, что путь заканчивается символом '/'
		}
		
		if ( boolAnyErrArguments == true )	// проверка на любые другие незадекларированные комбинации ключей и аргументов
		{
			intCommand = 0;
			
			System.out.println( "ERROR: incorrect keys or arguments, use '-h' for HELP" );
		}
		/*
		System.out.println( strKeyGit );
		System.out.println( strKeyMaster );
		System.out.println( strKeyHelp );
		*/
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
	            	if (strBuf.indexOf(strGitSearchMask)>=0)	// поиск подстроки .git в строках путей
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