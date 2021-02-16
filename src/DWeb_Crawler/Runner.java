package DWeb_Crawler;

/**
 * This tool allows a user to enter a search term or phrase in the command line
 * to pull the top deep web sites relating to that query using the darksearch.io API.
 *
 * The software is provided as is, without warranty of any kind, express or implied,
 * including but not limited to the warranties of merchantability,
 * fitness for a particular purpose and noninfringment. In no event shall the authors
 * or copyright holder be liable for any claim, damages or other liability, whether in an
 * action of contract, tort or otherwise, arising from, out of or in connection with the software
 * or the use of other dealings in the software.
 */
public class Runner {

    public static void main(String[] args){
        crawler c = new crawler();
        c.startup();
    }
}
