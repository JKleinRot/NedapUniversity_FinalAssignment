Instructions for installing and starting the game Go

- Install Java at your computer. Go to https://www.java.com/en/download/help/download_options.xml for instructions on how to install Java

- Download GoApplication.jar in the jar folder from my public repository on GitHub https://github.com/JKleinRot/NedapUniversity_FinalAssignment and save it on your computer

- Open a command terminal and go the directory where you saved GoApplication.jar

- Start each server or client in a new command terminal

	- To start a server type the expression below with a port number:
	
		java -cp GoApplication.jar server.GoServer <port number>

	- To start a client type the expression below with a name:

		java -cp GoApplication.jar client.GoClient <name>

- To stop the server, press ctrl + c

- To stop the client, type EXIT or press ctrl + c

