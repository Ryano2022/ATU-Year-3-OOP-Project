package ie.atu.sw;

import java.util.Scanner;

/**
 * @author Ryan Hogan
 * @version 1.0
 * @since 21
 * 
 * This class is the <b>main menu</b> of the application. 
 * It contains a main method and a method to print a progress meter.
 */
public class Runner {

	public static void main(String[] args) throws Exception {
		
		Scanner keyboard = new Scanner(System.in);
		int choice = 0, quit = 0;
		String lexiconFile = "";
		String inputFile = "";
		String outputFile = "./out.txt";
		
		do {
			//You should put the following code into a menu or Menu class
			System.out.println(ConsoleColour.WHITE);
			System.out.println("************************************************************");
			System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
			System.out.println("*                                                          *");
			System.out.println("*             Virtual Threaded Sentiment Analyser          *");
			System.out.println("*                                                          *");
			System.out.println("************************************************************");
			System.out.println("(1) Specify a Text File");
			System.out.println("(2) Specify a URL");
			System.out.println("(3) Specify an Output File (default: ./out.txt)");
			System.out.println("(4) Configure Lexicons");
			System.out.println("(5) Execute, Analyse and Report");
			System.out.println("(?) Optional Extras...");
			System.out.println("(-1) Quit");
			
			//Output a menu of options and solicit text from the user
			System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
			System.out.print("Select Option [1-4]>");
			System.out.println();
			System.out.print(ConsoleColour.WHITE);
			choice = keyboard.nextInt();
			
			if(choice == 1) {
				// Specify a text file.
				System.out.println("Enter the directory where the input file is located: ");
				inputFile = keyboard.next();
				System.out.println("File chosen: " + inputFile);
			}
			else if(choice == 2) {
				// Specify a URL.
				System.out.println("I don't know what I'm being asked to do here. ");
			}
			else if(choice == 3) {	
				// Specify an output file. (Default: ./out.txt)
				System.out.println("Enter the full path of the output file (default is ./out.txt) ");
				outputFile = keyboard.next();
				System.out.println("File chosen: " + outputFile);
			}
			else if(choice == 4) {
				// Configure lexicons.
				System.out.println("Enter the name and file extension of the input file (assume it is in ./lexicons): ");
				lexiconFile = "./lexicons/" + keyboard.next();
				System.out.println("Lexicon chosen: " + lexiconFile);
			}
			else if(choice == 5) {
				// Execute, analyse and report.
				if(inputFile.isEmpty()) {
					System.out.println("No input file specified. ");
				}
				else if(lexiconFile.isEmpty()) {
					System.out.println("No lexicon file specified. ");
				}
				else {
					VirtualThreadFileParser vtfp = new VirtualThreadFileParser();
					vtfp.go(inputFile, lexiconFile, outputFile);
				}
			}
			else if(choice == 6) {
				// Optional Extras
				System.out.println("I don't know what I could add as an extra. The project itself is very hard to wrap my head around as I've never done anything like it. ");
			}
			else {
				// Quit
				System.out.println("Terminated. ");
				quit = -1;
			}
		}while(quit != -1);
		
		//You may want to include a progress meter in you assignment!
		/*
		System.out.print(ConsoleColour.YELLOW);	//Change the colour of the console text
		int size = 100;							//The size of the meter. 100 equates to 100%
		for (int i =0 ; i < size ; i++) {		//The loop equates to a sequence of processing steps
			printProgress(i + 1, size); 		//After each (some) steps, update the progress meter
			Thread.sleep(10);					//Slows things down so the animation is visible 
		}
		*/
	}
	

	
	/*
	 *  Terminal Progress Meter
	 *  -----------------------
	 *  You might find the progress meter below useful. The progress effect 
	 *  works best if you call this method from inside a loop and do not call
	 *  System.out.println(....) until the progress meter is finished.
	 *  
	 *  Please note the following carefully:
	 *  
	 *  1) The progress meter will NOT work in the Eclipse console, but will
	 *     work on Windows (DOS), Mac and Linux terminals.
	 *     
	 *  2) The meter works by using the line feed character "\r" to return to
	 *     the start of the current line and writes out the updated progress
	 *     over the existing information. If you output any text between 
	 *     calling this method, i.e. System.out.println(....), then the next
	 *     call to the progress meter will output the status to the next line.
	 *      
	 *  3) If the variable size is greater than the terminal width, a new line
	 *     escape character "\n" will be automatically added and the meter won't
	 *     work properly.  
	 *  
	 * 
	 */
	public static void printProgress(int index, int total) {
		if (index > total) return;	//Out of range
      int size = 50; 						//Must be less than console width
	    char done = '█';					//Change to whatever you like.
	    char todo = '░';					//Change to whatever you like.
	    
	    //Compute basic metrics for the meter
        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;
        
        /*
         * A StringBuilder should be used for string concatenation inside a 
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.  
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
        	sb.append((i < completeLen) ? done : todo);
        }
        
        /*
         * The line feed escape character "\r" returns the cursor to the 
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        System.out.print("\r" + sb + "] " + complete + "%");
        
        //Once the meter reaches its max, move to a new line.
        if (done == total) System.out.println("\n");
    }
}