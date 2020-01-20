// Ryan Chiu, CSE 143A, Section AP
// Assignment #8
// The HuffmanCode class represents a Huffman code in the encoding & decoding process 
// by utilizing the Huffman algorithm. Text file being written or read by the program
// will be in the "Standard Format" specified.
import java.util.*;
import java.io.*;

public class HuffmanCode {
    public HuffmanNode overallRoot;

    // Constructor: takes an int Array frequencies as parameter.
    // - Assume: frequences[i] is the count of the character with ASCII value i.
    // Based on input array values, Constructs a HuffmanCode object at an initialized stage
    // , by using the Huffman algorithm.
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                HuffmanNode tmp = new HuffmanNode(i, frequencies[i]);
                q.add(tmp);
            }
        }
        while (q.size() > 1) {
            HuffmanNode first = q.remove();
            HuffmanNode second = q.remove();
            q.add(new HuffmanNode(0, first.freq + second.freq, first, second));
        }
        overallRoot = q.remove();
    }

    // Constructor: takes a Scanner input as parameter.
    // - Assume Scanner input is not null, and always passed in with data encoded in a valid
    //   "Standard format".
    // Construct a new HuffmanCode object at an initialized stage, by reading from the Scanner
    // input that contains a set of previously constructed huffman codes from a .code file.
    public HuffmanCode(Scanner input) {
        while (input.hasNextLine()) {
            int asciiValue = Integer.parseInt(input.nextLine());
            String hCode = input.nextLine();
            overallRoot = readCode(overallRoot, asciiValue, hCode);
        }
    }

    // Takes HuffmanNode root, int aValue, String hCode as parameter.
    // Returns HuffmanNode root initialized with ascii value as aValue when String hCode is empty.
    // Otherwise, Returns a dummy HuffmanNode with ascii value as 0 and 
    // continues iterating down the tree based on String hCode.
    // - Note: HuffmanNodes being returned are created with frequencies of -1 since freqencies
    //   are irrelevant when decoding.
    private HuffmanNode readCode(HuffmanNode root, int aValue, String hCode) {
        if (hCode.isEmpty()) {
            return new HuffmanNode(aValue, -1);
        } else {
            if (root == null) {
               root = new HuffmanNode(0, -1);
            }
            if (hCode.charAt(0) == '0') {
                root.left = readCode(root.left, aValue, hCode.substring(1));
            } else if (hCode.charAt(0) == '1') {
                root.right = readCode(root.right, aValue, hCode.substring(1));
            }
        }
        return root;
    }

    // Takes a PrintStream output as parameter.
    // Writes the current huffman codes to output stream in the "Standard Format".
    public void save(PrintStream output){
        save(output, overallRoot, "");
    }

    // Takes a PrintStream output, a HuffmanNode root, and a String soFar as parameter.
    // Iterates through the current tree - When input HuffmanNode root is a leaf node, 
    // writes input String soFar to the output PrintStream in "Standard Format".
    // Otherwise, keep iterating the tree until all HuffmanNodes are visited.
    private void save(PrintStream output, HuffmanNode root, String soFar) {
        if (root != null) {
            if (root.left == null && root.right == null) {
                output.println((int)root.letter);
                output.println(soFar);
            }
            save(output, root.left, soFar + 0);
            save(output, root.right, soFar + 1);
        }
    }

    // Takes BitInputStream input, PrintStream output as parameter.
    // - Assume BitInputStream input contains a legal, correctly encoded huffman codes
    //   using the current tree.
    // By reading bits using BitInputStream input, Translates the encoded codes into characters
    // and Writes these characters into PrintStream output
    public void translate(BitInputStream input, PrintStream output) { 
        HuffmanNode root = overallRoot;
        while (input.hasNextBit()) {
            int bit = input.nextBit();
            if (bit == 0) {
                root = root.left;
            } else {
                root = root.right;
            }
            
            if (root.left == null && root.right == null) {
                output.print(root.letter);
                root = overallRoot;
            }
        }
    }

    // The HuffmanNode class represents a node used in the parent class HuffmanCode.
    // - This class implements the Comparable Interface.
    private static class HuffmanNode implements Comparable<HuffmanNode> { 
        public int freq;
        public char letter;
        public HuffmanNode left;
        public HuffmanNode right;

        // Constructs a HuffmanNode with parameters int asciiCode and int freq.
        public HuffmanNode(int asciiCode, int freq) {
            this(asciiCode, freq, null, null);
        }

        // Takes int asciiCode, int freq, HuffmanNode left, HuffmanNode right as parameters.
        // Constructs a HuffmanNode with HuffmanNode left and HuffmanNode right
        // as its left and right node, and an int asciiCode and a specifed frequency.
        public HuffmanNode(int asciiCode, int freq, HuffmanNode left, HuffmanNode right) {
            this.letter = (char)asciiCode;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // Returns an int as difference between the frequencies of the HuffmanNode itself
        // and HuffmanNode other, which is passed in as a parameter.
        public int compareTo(HuffmanNode other) {
            return this.freq - other.freq;
        }
    }
}