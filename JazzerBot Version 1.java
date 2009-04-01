/* JazzerBot Version 1.6
 * This program is capable of improvising a jazz solo as well as playing
 * a basic blues accompaniment, chords and bassline included, using the key,
 * instrument, and tempo predefined by the user, either through the default
 * sound system or through an external MIDI device.
 * Created by Ben Wiley 2008-2009 */

import java.util.*;
import javax.sound.midi.*;
import java.io.*;

class JazzerBot{
  
  static boolean addmore;
  static boolean bassend;
  static int advance;

    public static void main(String [] args)
    throws MidiUnavailableException, InvalidMidiDataException, IOException {
      Scanner scan = new Scanner (System.in);
      int x = 1;
      int choice = 5;
      System.out.println();
      System.out.println("Welcome to JazzerBot, the amazing tool that");
      System.out.println("improvises jazz solos the way YOU want them!");
      while(x==1){
        while(!(choice==1||choice==2)){
          System.out.println();
          System.out.println("Press 1 to create a new solo or press 2 to open");
          System.out.println("and play an existing file, and press ENTER.");
          choice = scan.nextInt();
        }
        if(choice==1){
          Sequence sequence = new Sequence(Sequence.PPQ, 32);
          int tempo;
          for (tempo = 0; tempo < 70 || tempo > 170;) {
          System.out.println();
          System.out.println("Please enter a tempo for the solo between 70 and 170 BPM, and press ENTER.");
          tempo = scan.nextInt();
          }
          int instrument;
          for (instrument = -1; !(instrument>=0&&instrument<=5);) {
            System.out.println();
            System.out.println("Would you like the solo to be played on piano,");
            System.out.println("trumpet, saxophone, guitar, clarinet, or flute?");
            System.out.println("Press 0 for piano, 1 for trumpet, 2 for saxophone, 3 for guitar,");
            System.out.println("4 for clarinet, or 5 for flute, and press ENTER.");
            instrument = scan.nextInt();
          }
          if (instrument==1) instrument = 56;
          if (instrument==2) instrument = 64;
          if (instrument==3) instrument = 26; //might be changed
          if (instrument==4) instrument = 71;
          if (instrument==5) instrument = 73;
          
          for(addmore=true; addmore; ){
            main1(sequence, tempo, instrument);
            for (int u = 2; u!=0&&u!=1;){
              System.out.println();
              System.out.println("Would you like to add additional, differently formatted measures to the");
              System.out.println("solo (press 1 and press ENTER) or finish (press 0 and press ENTER)?");
              u=scan.nextInt();scan.nextLine();
              if (u==1){}
              else addmore = false;
            }
          }
          Sequencer sequencer;
          int mididevice1 = 100;
          while(mididevice1!=0 && mididevice1!=1){
            System.out.println();
          System.out.println("Would you like to send the audio to an external MIDI device (press 1,");
          System.out.println("and press ENTER) or use the default system (press 0, and press ENTER)?");
          mididevice1 = scan.nextInt();}
          if(mididevice1 == 1){
            System.out.println();
            System.out.println("Choose a MIDI output device by entering the corresponding number and pressing ENTER:");
            MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
            for (int i = 0; i < aInfos.length; i++) {
              MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
              boolean  bAllowsOutput = (device.getMaxReceivers() != 0);
              if ((bAllowsOutput)) {
                System.out.println(""+i+"  "
                +aInfos[i].getName()+", "
                +aInfos[i].getVendor()+", "
                +aInfos[i].getVersion()+", "
                +aInfos[i].getDescription());
              }
            }
            int mididevice = scan.nextInt();
            MidiDevice exdevice = MidiSystem.getMidiDevice(aInfos[mididevice]);
            exdevice.open();
            sequencer = MidiSystem.getSequencer(false);
            sequencer.getTransmitter().setReceiver(exdevice.getReceiver());
            sequencer.open();
          }
          else{
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
          }
        
          sequencer.setSequence(sequence);
          sequencer.setTempoInBPM(tempo);

          /* int countrepeat = 1;
          int endanswer = 4;
          for(;countrepeat == 1;){
          sequencer.addMetaEventListener(new MetaEventListener( ) {
          public void meta(MetaMessage m) {
          // A message of this type is automatically sent
          // when we reach the end of the track
          if (m.getType( ) == END_OF_TRACK){
          System.out.println("The solo you created has ended.");
          System.out.println("Press 1 if you would like it to repeat again,");
          System.out.println("press 2 if you would like to set criteria for a");
          System.out.println("new solo, or press 0 to exit.");
          endanswer = scan.nextLine();
          if (endanswer == 1)
          System.exit(0);
          }
          }
          }); */

          String dope, lol;
          System.out.println();
          System.out.println("Press enter to play.");
          dope = scan.nextLine();
          lol = scan.nextLine();
        
          //START PLAYING
          sequencer.start();
        
          System.out.println();
          System.out.println("Would you like to save this sequence to a file? (1 for yes, 0 for no, and press ENTER)");
          int yno=scan.nextInt();
          if(yno==1){
            System.out.println();
            System.out.println("Please write the desired filename, and press ENTER.");
            String filename1 = scan.nextLine();
            String filename = scan.nextLine();
            if(filename.endsWith(".midi")){
              filename=filename;
            } 
            else{
              filename += ".midi";
            }
            FileOutputStream file = new FileOutputStream(filename);
            MidiSystem.write(sequence, 1, file);
            System.out.println("Your file may now be found in the same directory in which JazzerBot is located.");
          }
          System.out.println();
          System.out.println("To play another solo, press 1, or to exit, press 0, and press ENTER.");
          int exit = scan.nextInt();
          if(exit==1){
          }
          else
            System.exit(0);
        }
        if(choice==2)
          main2();
        choice = 5;
      }
    }

    public static void main2()
            throws MidiUnavailableException, InvalidMidiDataException, IOException {

        Scanner scan = new Scanner(System.in);
        
        System.out.println();
        System.out.println("Please enter the filename, and press ENTER:");
        String filename = scan.nextLine();
        System.out.println();
        System.out.println("Enter a tempo for the sequence to use, and press ENTER:");
        int tempo = scan.nextInt();
        
        if(filename.endsWith(".midi")){
          filename=filename;
        } 
        else{
          filename += ".midi";
        }
        FileInputStream file = new FileInputStream(filename);
          
        Sequencer sequencer;
        int mididevice1 = 100;
        while(mididevice1!=0 && mididevice1!=1){
          System.out.println();
        System.out.println("Would you like to send the audio to an external MIDI device (press 1,");
        System.out.println("and press ENTER) or use the default system (press 0, and press ENTER)?");
        mididevice1 = scan.nextInt();}
        if(mididevice1 == 1){
          System.out.println();
          System.out.println("Choose a MIDI output device by entering the corresponding number and pressing ENTER:");
          MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
          for (int i = 0; i < aInfos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
            boolean  bAllowsOutput = (device.getMaxReceivers() != 0);
            if ((bAllowsOutput)) {
              System.out.println(""+i+"  "
              +aInfos[i].getName()+", "
              +aInfos[i].getVendor()+", "
              +aInfos[i].getVersion()+", "
              +aInfos[i].getDescription());
            }
          }
          int mididevice = scan.nextInt();
          MidiDevice exdevice = MidiSystem.getMidiDevice(aInfos[mididevice]);
          exdevice.open();
          sequencer = MidiSystem.getSequencer(false);
          sequencer.getTransmitter().setReceiver(exdevice.getReceiver());
          sequencer.open();
        }
        else{
          sequencer = MidiSystem.getSequencer();
          sequencer.open();
          Synthesizer synth = MidiSystem.getSynthesizer();
          synth.open();
        }
          
        sequencer.setSequence(MidiSystem.getSequence(file));
        sequencer.setTempoInBPM(tempo);
          
        String dope;
        System.out.println();
        System.out.println("Press enter to play.");
        dope = scan.nextLine();scan.nextLine();
        
        //START PLAYING
        sequencer.start();
          
        System.out.println();
        System.out.println("To play another solo, press 1, or to exit, press 0, and press ENTER.");
        int exit = scan.nextInt();
        if(exit==1){
        }
        else
          System.exit(0);    
    }
    
    public static void main1(Sequence sequence, int tempo, int instrument)
            throws MidiUnavailableException, InvalidMidiDataException, IOException {

        Scanner scan = new Scanner(System.in);

        String keysig = "0";

        int measures;
        boolean four = false;

        while (keysig.equals("0")) {
            System.out.println();
            System.out.println("Please input the key in which you want this solo to be");
            System.out.println("played by entering the corresponding character and pressing ENTER");
            System.out.println("(This is case-sensitive):");
            System.out.println("A- A                B Flat- b");
            System.out.println("B- B                C- C");
            System.out.println("D Flat- d           D- D");
            System.out.println("E Flat- e           E- E");
            System.out.println("F- F                G Flat- g");
            System.out.println("G- G                A Flat- a");

            char keysiginput = scan.next().charAt(0);

            switch (keysiginput) {
                case 'A':
                    keysig = "A";
                    break;
                case 'b':
                    keysig = "Bb";
                    break;
                case 'B':
                    keysig = "B";
                    break;
                case 'C':
                    keysig = "C";
                    break;
                case 'd':
                    keysig = "Db";
                    break;
                case 'D':
                    keysig = "D";
                    break;
                case 'e':
                    keysig = "Eb";
                    break;
                case 'E':
                    keysig = "E";
                    break;
                case 'F':
                    keysig = "F";
                    break;
                case 'g':
                    keysig = "Gb";
                    break;
                case 'G':
                    keysig = "G";
                    break;
                case 'a':
                    keysig = "Ab";
                    break;
                default:
                    keysig = "0";
                    break;
            }
        }

        //future location of time signature selector???

        int measures1;
        for (measures1 = 20; measures1 % 12 != 0;) {
          System.out.println();
            System.out.println("How many measures should the solo last? (Input a");
            System.out.println("multiple of 12, and press ENTER)");
            measures1 = scan.nextInt();
        }
        measures = measures1 / 12;
        
        System.out.println();
        System.out.println("Would you like to trade fours with JazzerBot (take turns");
        System.out.println("playing four measure solos)? (Press 1 if yes, 0, if no, and");
        System.out.println("press ENTER.");
        int hmm = scan.nextInt();
        if(hmm==1)four = true; else {}
        
        for(int eh = 17; eh != 0 && eh !=1;){
          System.out.println();
          System.out.println("Would you like the accompaniment to feature an alternate ending on the final measure?");
          System.out.println("If so, press 1, or if not, press 0, and press ENTER.");
          eh = scan.nextInt();
          if (eh == 1) bassend = true;
          else bassend = false;
        }
       
        int bassfff;
        for(bassfff = 2; bassfff != 0 && bassfff!=1;){
          System.out.println();
          System.out.println("Would you like the bass to be turned on or off?");
          System.out.println("For on, press 1, or for off, press 0, and press ENTER.");
          bassfff = scan.nextInt();
        }
        
        int chordfff;
        for(chordfff = 2; chordfff != 0 && chordfff!=1;){
          System.out.println();
          System.out.println("Would you like the chords to be turned on or off?");
          System.out.println("For on, press 1, or for off, press 0, and press ENTER.");
          chordfff = scan.nextInt();
        }

        int solofff;
        for(solofff = 2; solofff != 0 && solofff!=1;){
          System.out.println();
          System.out.println("Would you like the solo to be turned on or off?");
          System.out.println("For on, press 1, or for off, press 0, and press ENTER.");
          solofff = scan.nextInt();
        }

        if (bassfff == 1) {
            addBass(sequence, measures, keysig);
        }

        if (chordfff == 1) {
            addChord(sequence, measures, keysig);
        }

        if (solofff == 1) {
            addSolo(sequence, instrument, measures, keysig, tempo, four);
        }
        
        advance+=measures*1536;
    }

    public static void addBass(Sequence s, int measures, String keysig)
            throws InvalidMidiDataException {

        Track basstrack = s.createTrack();
        int velocity = 90;

        ShortMessage smbass = new ShortMessage();
        smbass.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 32, 0);
        basstrack.add(new MidiEvent(smbass, 0));

        int trans;
        //splitting point: E Flat/E (or possible D/E Flat)

        if (keysig.equals("E")) trans = 0;
        else if (keysig.equals("F")) trans = 1;
        else if (keysig.equals("Gb")) trans = 2;
        else if (keysig.equals("G")) trans = 3;
        else if (keysig.equals("Ab")) trans = 4;
        else if (keysig.equals("A")) trans = 5;
        else if (keysig.equals("Bb")) trans = 6;
        else if (keysig.equals("B")) trans = 7;
        else if (keysig.equals("C")) trans = 8;
        else if (keysig.equals("Db")) trans = 9;
        else if (keysig.equals("D")) trans = 10;
        else if (keysig.equals("Eb")) trans = 11;
        else trans = 0;

        //note length options (for rest use t += ___)
        int half = 64;
        int quarter = 32;
        int eight1 = 22;
        int eight2 = 10;

        int t = 0;

        for (int count = 0; count < measures; count++) {
            t = count * 1536 + advance;

            //addNote(Track, startTick, tickLength, Key, Velocity)
            addNote1(basstrack, 0 + t, quarter, 40 + trans, velocity);
            addNote1(basstrack, 32 + t, quarter, 44 + trans, velocity);
            addNote1(basstrack, 64 + t, quarter, 47 + trans, velocity);
            addNote1(basstrack, 96 + t, quarter, 44 + trans, velocity);
            addNote1(basstrack, 128 + t, quarter, 33 + trans, velocity);
            addNote1(basstrack, 160 + t, quarter, 37 + trans, velocity);
            addNote1(basstrack, 192 + t, quarter, 38 + trans, velocity);
            addNote1(basstrack, 224 + t, quarter, 39 + trans, velocity);
            addNote1(basstrack, 256 + t, quarter, 40 + trans, velocity);
            addNote1(basstrack, 288 + t, quarter, 44 + trans, velocity);
            addNote1(basstrack, 320 + t, quarter, 47 + trans, velocity);
            addNote1(basstrack, 352 + t, quarter, 44 + trans, velocity);
            addNote1(basstrack, 384 + t, eight2, 40 + trans, velocity);
            addNote1(basstrack, 406 + t, eight2, 40 + trans, velocity);
            addNote1(basstrack, 416 + t, quarter, 38 + trans, velocity);
            addNote1(basstrack, 448 + t, quarter, 37 + trans, velocity);
            addNote1(basstrack, 480 + t, quarter, 35 + trans, velocity);
            addNote1(basstrack, 512 + t, quarter, 33 + trans, velocity);
            addNote1(basstrack, 544 + t, quarter, 37 + trans, velocity);
            addNote1(basstrack, 576 + t, quarter, 38 + trans, velocity);
            addNote1(basstrack, 608 + t, quarter, 39 + trans, velocity);
            addNote1(basstrack, 640 + t, quarter, 40 + trans, velocity);
            addNote1(basstrack, 672 + t, quarter, 37 + trans, velocity);
            addNote1(basstrack, 704 + t, quarter, 38 + trans, velocity);
            addNote1(basstrack, 736 + t, quarter, 39 + trans, velocity);
            addNote1(basstrack, 768 + t, quarter, 40 + trans, velocity);
            addNote1(basstrack, 800 + t, quarter, 44 + trans, velocity);
            addNote1(basstrack, 832 + t, quarter, 47 + trans, velocity);
            addNote1(basstrack, 864 + t, quarter, 44 + trans, velocity);
            addNote1(basstrack, 896 + t, eight2, 40 + trans, velocity);
            addNote1(basstrack, 918 + t, eight2, 40 + trans, velocity);
            addNote1(basstrack, 928 + t, quarter, 38 + trans, velocity);
            addNote1(basstrack, 960 + t, quarter, 37 + trans, velocity);
            addNote1(basstrack, 992 + t, quarter, 35 + trans, velocity);
            addNote1(basstrack, 1024 + t, quarter, 35 + trans, velocity);
            addNote1(basstrack, 1024 + t, quarter, 35 + trans, velocity);
            addNote1(basstrack, 1056 + t, quarter, 39 + trans, velocity);
            addNote1(basstrack, 1088 + t, quarter, 42 + trans, velocity);
            addNote1(basstrack, 1120 + t, quarter, 39 + trans, velocity);
            addNote1(basstrack, 1152 + t, quarter, 33 + trans, velocity);
            addNote1(basstrack, 1184 + t, quarter, 37 + trans, velocity);
            addNote1(basstrack, 1216 + t, quarter, 38 + trans, velocity);
            addNote1(basstrack, 1248 + t, quarter, 39 + trans, velocity);

            if (count + 1 != measures || (count + 1 == measures && !bassend)) {
                addNote1(basstrack, 1280 + t, quarter, 40 + trans, velocity);
                addNote1(basstrack, 1312 + t, quarter, 44 + trans, velocity);
                addNote1(basstrack, 1344 + t, quarter, 47 + trans, velocity);
                addNote1(basstrack, 1376 + t, quarter, 44 + trans, velocity);
                addNote1(basstrack, 1408 + t, quarter, 35 + trans, velocity);
                addNote1(basstrack, 1440 + t, quarter, 39 + trans, velocity);
                addNote1(basstrack, 1472 + t, quarter, 42 + trans, velocity);
                addNote1(basstrack, 1504 + t, quarter, 39 + trans, velocity);
            } else {
                addNote1(basstrack, 1280 + t, quarter, 40 + trans, velocity);
                addNote1(basstrack, 1312 + t, quarter, 44 + trans, velocity);
                addNote1(basstrack, 1344 + t, quarter, 45 + trans, velocity);
                addNote1(basstrack, 1376 + t, quarter, 46 + trans, velocity);
                addNote1(basstrack, 1408 + t, eight1, 47 + trans, velocity);
                addNote1(basstrack, 1430 + t, eight2, 45 + trans, velocity);
                addNote1(basstrack, 1440 + t, eight1, 44 + trans, velocity);
                addNote1(basstrack, 1462 + t, eight2 + half, 40 + trans, velocity);
            }
        }
    }

    static void addChord(Sequence s, int measures, String keysig)
            throws InvalidMidiDataException {

        Track chordtrack = s.createTrack();
        int velocity = 65;

        ShortMessage smchord = new ShortMessage();
        smchord.setMessage(ShortMessage.PROGRAM_CHANGE, 1, 0, 0);
        chordtrack.add(new MidiEvent(smchord, 0));

        int trans;
        //inversion point: E Flat/E
        //all between D (50) and D Flat (61)

        if (keysig.equals("E")) trans = 0;
        else if (keysig.equals("F")) trans = 1;
        else if (keysig.equals("Gb")) trans = 2;
        else if (keysig.equals("G")) trans = 3;
        else if (keysig.equals("Ab")) trans = 4;
        else if (keysig.equals("A")) trans = 5;
        else if (keysig.equals("Bb")) trans = 6;
        else if (keysig.equals("B")) trans = 7;
        else if (keysig.equals("C")) trans = 8;
        else if (keysig.equals("Db")) trans = 9;
        else if (keysig.equals("D")) trans = 10;
        else if (keysig.equals("Eb")) trans = 11;
        else trans = 0;

        int a1 = 50 + trans;
        int b1 = 56 + trans;
        int c1 = 59 + trans;
        int a4 = 55 + trans;
        int b4 = 61 + trans;
        int c4 = 52 + trans;
        int a5 = 57 + trans;
        int b5 = 51 + trans;
        int c5 = 54 + trans;

        if (a1 > 61) {
            a1 -= 12;
        }
        if (b1 > 61) {
            b1 -= 12;
        }
        if (c1 > 61) {
            c1 -= 12;
        }
        if (a4 > 61) {
            a4 -= 12;
        }
        if (b4 > 61) {
            b4 -= 12;
        }
        if (c4 > 61) {
            c4 -= 12;
        }
        if (a5 > 61) {
            a5 -= 12;
        }
        if (b5 > 61) {
            b5 -= 12;
        }
        if (c5 > 61) {
            c5 -= 12;
        }

        //note length options (for rest use t += ___)
        //int whole = 128;
        //int eight1 = 22;
        //int eight2 = 10;

        int t = 0;

        for (int count = 0; count < measures; count++) {
            t = count * 1536 + advance;

            //addChords(number, track, startTick, first, second, third, velocity)
            addChords(.9, chordtrack, 0 + t, a1, b1, c1, velocity);
            addChords(Math.random(), chordtrack, 128 + t, a4, b4, c4, velocity);
            addChords(Math.random(), chordtrack, 256 + t, a1, b1, c1, velocity);
            addChords(Math.random(), chordtrack, 384 + t, a1, b1, c1, velocity);
            addChords(Math.random(), chordtrack, 512 + t, a4, b4, c4, velocity);
            addChords(Math.random(), chordtrack, 640 + t, a4, b4, c4, velocity);
            addChords(Math.random(), chordtrack, 768 + t, a1, b1, c1, velocity);
            addChords(Math.random(), chordtrack, 896 + t, a1, b1, c1, velocity);
            addChords(Math.random(), chordtrack, 1024 + t, a5, b5, c5, velocity);
            addChords(Math.random(), chordtrack, 1152 + t, a4, b4, c4, velocity);
            addChords(Math.random(), chordtrack, 1280 + t, a1, b1, c1, velocity);

            if (count + 1 != measures || (count + 1 == measures && !bassend)) {
                addChords(Math.random(), chordtrack, 1408 + t, a4, b4, c4, velocity);
            } else {
                addChords(Math.random(), chordtrack, 1408 + t, a1, b1, c1, velocity);
            }
        }
    }

    public static void addSolo(Sequence s, int instrument, int measures, String keysig, int tempo, boolean four)
            throws InvalidMidiDataException {

        Track solotrack = s.createTrack();  // Begin with a new track

        // Set the instrument on channel 0
        ShortMessage sm = new ShortMessage();
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, 2, instrument, 0);
        solotrack.add(new MidiEvent(sm, 0));
        
        double p; // random variable
        int lastdir = 1; // last direction in which the melody moved; 1 is up, -1 is down
        int lastnote = 0; // the last key that was played
        int t = 0; // indicates the current tick in the course of the solo

        // full blast for solo
        int velocity = 90;

        //note length options (for rest use t += ___)
        int twhole = 256;
        int whole = 128;
        int half = 64;
        int quarter = 32;
        int eight1 = 22;
        int eight2 = 10;
        int straighteight = 16;
        int ssixteen = 8;


        int basekey = 60;    // 60 is middle C
        int octave = 12;

        

        for (int count = 0; count < measures; count++) {
            t = count * 1536+advance;

            for (int mcount = 0; mcount < 12; mcount += 2) {
                double rhythm1 = Math.random();
                int aa, bb, cc, dd, ee, ff, gg, hh, ii, jj, kk, ll, mm, nn, oo, 
                  pp, qq, rr, ss, tt, uu, vv, ww, xx, yy, zz;
                
                int low; // low constraint for melodic section
                int high;// high constraint
                if(keysig.equals("C")){low=-1;high=13;}
                else if(keysig.equals("Db")){low=-1;high=12;}
                else if(keysig.equals("D")){low=-1;high=12;}
                else if(keysig.equals("Eb")){low=-2;high=12;}
                else if(keysig.equals("E")){low=-3;high=11;}
                else if(keysig.equals("F")){low=-4;high=11;}
                else if(keysig.equals("Gb")){low=-4;high=10;}
                else if(keysig.equals("G")){low=-5;high=10;}
                else if(keysig.equals("Ab")){low=-5;high=10;}
                else if(keysig.equals("A")){low=-5;high=9;}
                else if(keysig.equals("Bb")){low=-6;high=8;}
                else if(keysig.equals("B")){low=-6;high=7;}
                else {low=0;high=0;}
                
                boolean fourtron;
                if (!(count%2==0)){
                  if (mcount==0||mcount==2||mcount==8||mcount==10) fourtron = true;
                  else fourtron = false;
                }
                else{
                  if (mcount==4||mcount==6) fourtron = true;
                  else fourtron = false;
                }
                
                if (four&&fourtron){
                }
                
                else{
                
                //13% possibility of 2 measure rest on the odd two measure sets
                if(rhythm1>=.89 && (mcount/2)%2==0){
                }
                
                //chord options
                else if(rhythm1>=.83 && !(mcount == 10 && count+1==measures) && instrument == 0){
                  double chordd = Math.random();
                  int trans;
                  if (keysig.equals("E")) trans = 0;
                  else if (keysig.equals("F")) trans = 1;
                  else if (keysig.equals("Gb")) trans = 2;
                  else if (keysig.equals("G")) trans = 3;
                  else if (keysig.equals("Ab")) trans = 4;
                  else if (keysig.equals("A")) trans = 5;
                  else if (keysig.equals("Bb")) trans = 6;
                  else if (keysig.equals("B")) trans = 7;
                  else if (keysig.equals("C")) trans = 8;
                  else if (keysig.equals("Db")) trans = 9;
                  else if (keysig.equals("D")) trans = 10;
                  else if (keysig.equals("Eb")) trans = 11;
                  else trans = 0;

                  int a1 = 50+24 + trans;
                  int b1 = 56+24 + trans;
                  int c1 = 59+24 + trans;
                  int a4 = 55+24 + trans;
                  int b4 = 61+24 + trans;
                  int c4 = 52+24 + trans;
                  int a5 = 57+24 + trans;
                  int b5 = 51+24 + trans;
                  int c5 = 54+24 + trans;

        
                  if (a1 > 61+24) {
                      a1 -= 12;
                  }
                  if (b1 > 61+24) {
                      b1 -= 12;
                  }
                  if (c1 > 61+24) {
                      c1 -= 12;
                  }
                  if (a4 > 61+24) {
                      a4 -= 12;
                  }
                  if (b4 > 61+24) {
                      b4 -= 12;
                  }
                  if (c4 > 61+24) {
                      c4 -= 12;
                  }
                  if (a5 > 61+24) {
                      a5 -= 12;
                  }
                  if (b5 > 61+24) {
                      b5 -= 12;
                  }
                  if (c5 > 61+24) {
                      c5 -= 12;
                  }
                  
                  if(mcount==0){
                    aa = a1; bb = b1; cc = c1;
                    dd = a4; ee = b4; ff = c4;
                  }
                  else if(mcount==2||mcount==6||(mcount==10 && count+1 == measures)){
                    aa = a1; bb = b1; cc = c1;
                    dd = a1; ee = b1; ff = c1;
                  }
                  else if(mcount==4){
                    aa = a4; bb = b4; cc = c4;
                    dd = a4; ee = b4; ff = c4;
                  }
                  else if(mcount==8){
                    aa = a5; bb = b5; cc = c5;
                    dd = a4; ee = b4; ff = c4;
                  }
                  else{
                    aa = a1; bb = b1; cc = c1;
                    dd = a5; ee = b5; ff = c5;
                  }
        
                  int y = 0;
                  if (keysig.equals("C")||keysig.equals("Db")||keysig.equals("D")||keysig.equals("Eb"))
                    y = 6;
                  if(chordd<.65){
                    //first chord (1)
                    addNote(solotrack, eight1+t, half+quarter, aa, velocity-15);
                    addNote(solotrack, eight1+t, half+quarter, bb, velocity-15);
                    addNote(solotrack, eight1+t, half+quarter, cc, velocity-15);
                    addNote(solotrack, half+quarter+eight1+t, whole, aa, velocity-15);
                    addNote(solotrack, half+quarter+eight1+t, whole, bb, velocity-15);
                    addNote(solotrack, half+quarter+eight1+t, whole, cc, velocity-15);
                    lastnote = 6+y;
                    
                    if (Math.random()<.28 && mcount<10 && !(mcount==8 && count+1 == measures) && !four){
                      t+=twhole;
                      mcount+=2;
                      if(mcount==0){
                        aa = a1; bb = b1; cc = c1;
                        dd = a4; ee = b4; ff = c4;
                      }
                      else if(mcount==2||mcount==6||(mcount==10 && count+1 == measures)){
                        aa = a1; bb = b1; cc = c1;
                        dd = a1; ee = b1; ff = c1;
                      }
                      else if(mcount==4){
                        aa = a4; bb = b4; cc = c4;
                        dd = a4; ee = b4; ff = c4;
                      }
                      else if(mcount==8){
                        aa = a5; bb = b5; cc = c5;
                        dd = a4; ee = b4; ff = c4;
                      }
                      else{
                        aa = a1; bb = b1; cc = c1;
                        dd = a5; ee = b5; ff = c5;
                      }
                      //opt added chord (2)
                      addNote(solotrack, t-eight2, eight2, aa, velocity-15);
                      addNote(solotrack, t-eight2, eight2, bb, velocity-15);
                      addNote(solotrack, t-eight2, eight2, cc, velocity-15);
                      addNote(solotrack, eight1+t, eight2, aa, velocity-15);
                      addNote(solotrack, eight1+t, eight2, bb, velocity-15);
                      addNote(solotrack, eight1+t, eight2, cc, velocity-15);
                      addNote(solotrack, quarter+eight1+t, half, aa, velocity-15);
                      addNote(solotrack, quarter+eight1+t, half, bb, velocity-15);
                      addNote(solotrack, quarter+eight1+t, half, cc, velocity-15);
                      addNote(solotrack, quarter+half+eight1+t, eight2, aa, velocity-15);
                      addNote(solotrack, quarter+half+eight1+t, eight2, bb, velocity-15);
                      addNote(solotrack, quarter+half+eight1+t, eight2, cc, velocity-15);
                      addNote(solotrack, whole+eight1/2+t, eight2, dd, velocity-15);
                      addNote(solotrack, whole+eight1/2+t, eight2, ee, velocity-15);
                      addNote(solotrack, whole+eight1/2+t, eight2, ff, velocity-15);
                      addNote(solotrack, whole+eight1+t, eight2, dd, velocity-15);
                      addNote(solotrack, whole+eight1+t, eight2, ee, velocity-15);
                      addNote(solotrack, whole+eight1+t, eight2, ff, velocity-15);
                      addNote(solotrack, whole+quarter+eight1/2+t, eight2, dd, velocity-15);
                      addNote(solotrack, whole+quarter+eight1/2+t, eight2, ee, velocity-15);
                      addNote(solotrack, whole+quarter+eight1/2+t, eight2, ff, velocity-15);
                      addNote(solotrack, whole+quarter+eight1+t, eight2, dd, velocity-15);
                      addNote(solotrack, whole+quarter+eight1+t, eight2, ee, velocity-15);
                      addNote(solotrack, whole+quarter+eight1+t, eight2, ff, velocity-15);
                      
                      aa = findBlues(6+y, keysig);
                      bb = findBlues(5+y, keysig);
                      cc = findBlues(4+y, keysig);
                      dd = findBlues(3+y, keysig);
                      addNote(solotrack, whole+half+t, eight1, aa, velocity-15);
                      addNote(solotrack, whole+half+eight1+t, eight2, bb, velocity-15);
                      addNote(solotrack, whole+half+quarter+t, eight1, cc, velocity-15);
                      addNote(solotrack, whole+half+quarter+eight1+t, eight2, dd, velocity-15);
                      lastnote = 3+y;
                    }
                  }
                  
                  else{
                    //third chord (3)
                    addNote(solotrack, 0+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, 0+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, 0+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, eight1/2+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, eight1/2+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, eight1/2+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, eight1+t, eight2, aa, velocity-15);
                    addNote(solotrack, eight1+t, eight2, bb, velocity-15);
                    addNote(solotrack, eight1+t, eight2, cc, velocity-15);
                    addNote(solotrack, quarter+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, quarter+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, quarter+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, quarter+eight1/2+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, quarter+eight1/2+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, quarter+eight1/2+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, quarter+eight1+t, eight2, aa, velocity-15);
                    addNote(solotrack, quarter+eight1+t, eight2, bb, velocity-15);
                    addNote(solotrack, quarter+eight1+t, eight2, cc, velocity-15);
                    addNote(solotrack, half+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, half+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, half+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, half+eight1/2+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, half+eight1/2+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, half+eight1/2+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, half+eight1+t, eight2, aa, velocity-15);
                    addNote(solotrack, half+eight1+t, eight2, bb, velocity-15);
                    addNote(solotrack, half+eight1+t, eight2, cc, velocity-15);
                    addNote(solotrack, half+quarter+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, half+quarter+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, half+quarter+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, half+quarter+eight1/2+t, eight1/2, aa, velocity-15);
                    addNote(solotrack, half+quarter+eight1/2+t, eight1/2, bb, velocity-15);
                    addNote(solotrack, half+quarter+eight1/2+t, eight1/2, cc, velocity-15);
                    addNote(solotrack, half+quarter+eight1+t, eight2, aa, velocity-15);
                    addNote(solotrack, half+quarter+eight1+t, eight2, bb, velocity-15);
                    addNote(solotrack, half+quarter+eight1+t, eight2, cc, velocity-15);
                    addNote(solotrack, whole+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+eight1/2+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+eight1/2+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+eight1/2+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+eight1+t, eight2, dd, velocity-15);
                    addNote(solotrack, whole+eight1+t, eight2, ee, velocity-15);
                    addNote(solotrack, whole+eight1+t, eight2, ff, velocity-15);
                    addNote(solotrack, whole+quarter+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+quarter+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+quarter+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+quarter+eight1/2+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+quarter+eight1/2+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+quarter+eight1/2+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, dd, velocity-15);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, ee, velocity-15);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, ff, velocity-15);
                    addNote(solotrack, whole+half+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+half+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+half+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+half+eight1/2+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+half+eight1/2+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+half+eight1/2+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+half+eight1+t, eight2, dd, velocity-15);
                    addNote(solotrack, whole+half+eight1+t, eight2, ee, velocity-15);
                    addNote(solotrack, whole+half+eight1+t, eight2, ff, velocity-15);
                    addNote(solotrack, whole+half+quarter+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+half+quarter+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+half+quarter+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+half+quarter+eight1/2+t, eight1/2, dd, velocity-15);
                    addNote(solotrack, whole+half+quarter+eight1/2+t, eight1/2, ee, velocity-15);
                    addNote(solotrack, whole+half+quarter+eight1/2+t, eight1/2, ff, velocity-15);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, dd, velocity-15);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, ee, velocity-15);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, ff, velocity-15);
                    lastnote = 6+y;
                  }
                  lastdir = -1;
                }
                 //melodies
                else{
                  double rhythm;
                  if(!(count+1==measures&&mcount==10))
                    rhythm = Math.random();
                  else
                    rhythm = Math.random()/2;
                  
                  // NUMBER ONE RIGHT HERE (8)
                  if(rhythm<.05&&mcount==0){
                    if(mcount == 0){
                      aa = 0;
                      bb = 2;
                    }
                    else if(mcount == 2||mcount == 6||(mcount == 10 && count+1 == measures)){
                      aa = 0;
                      bb = 0;
                    }
                    else if(mcount == 4){
                      aa = 2;
                      bb = 2;
                    }
                    else if(mcount == 8){
                      aa = 4;
                      bb = 2;
                    }
                    else{
                      aa = 0;
                      bb = 4;
                    }
                    
                    lastnote = bb;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, quarter, aa, velocity);
                    addNote(solotrack, quarter+t, quarter, aa, velocity);
                    addNote(solotrack, half+t, quarter, aa, velocity);
                    addNote(solotrack, whole+t, quarter, bb, velocity);
                    addNote(solotrack, whole+quarter+t, quarter, bb, velocity);
                    addNote(solotrack, whole+half+t, quarter, bb, velocity);
                  }
                  // NUMBER TWO RIGHT HERE (1)
                  if(rhythm<.10){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                      }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    p = Math.random();
                    if(gg-1>=low&&gg+1<=high){
                    if(p<.5) hh = gg+lastdir;
                    else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                    else hh = gg;}
                    else if(gg-1<low){hh = gg+1; lastdir = 1;}
                    else{hh = gg-1; lastdir = -1;}
                    p = Math.random();
                    if(hh-1>=low&&hh+1<=high){
                    if(p<.5) ii = hh+lastdir;
                    else if(p<.8){ii = hh-lastdir; lastdir = -lastdir;}
                    else ii = hh;}
                    else if(hh-1<low){ii = hh+1; lastdir = 1;}
                    else{ii = hh-1; lastdir = -1;}
                    p = Math.random();
                    if(ii-1>=low&&ii+1<=high){
                    if(p<.5) jj = ii+lastdir;
                    else if(p<.8){jj = ii-lastdir; lastdir = -lastdir;}
                    else jj = ii;}
                    else if(ii-1<low){jj = ii+1; lastdir = 1;}
                    else{jj = ii-1; lastdir = -1;}
                    p = Math.random();
                    if(jj-1>=low&&jj+1<=high){
                    if(p<.5) kk = jj+lastdir;
                    else if(p<.8){kk = jj-lastdir; lastdir = -lastdir;}
                    else kk = jj;}
                    else if(jj-1<low){kk = jj+1; lastdir = 1;}
                    else{kk = jj-1; lastdir = -1;}
                    
                    lastnote = kk;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig); ii = findBlues(ii, keysig);
                    jj = findBlues(jj, keysig); kk = findBlues(kk, keysig); 
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, eight1+t, eight2, aa, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, bb, velocity);
                    addNote(solotrack, half+eight1+t, quarter, cc, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, whole+t, eight1, ee, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, ff, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, gg, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, hh, velocity);
                    if(!(count+1==measures&&mcount==10)){
                    addNote(solotrack, whole+half+eight1+t, eight2, ii, velocity);
                    addNote(solotrack, whole+half+quarter+t, eight1, jj, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, kk, velocity);
                    }
                  }
                  // NUMBER THREE RIGHT HERE (2)
                  else if(rhythm<.15){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    
                    lastnote = gg;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, eight1+t, eight2, aa, velocity);
                    addNote(solotrack, quarter+t, eight1/2, bb, velocity);
                    addNote(solotrack, quarter+eight1/2+t, eight1/2, cc, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, half+t, eight1/2, bb, velocity);
                    addNote(solotrack, half+eight1/2+t, eight1/2, cc, velocity);
                    addNote(solotrack, half+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, half+quarter+t, eight1/2, bb, velocity);
                    addNote(solotrack, half+quarter+eight1/2+t, eight1/2, cc, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, dd, velocity);
                    if(!(count+1==measures&&mcount==10)){
                    addNote(solotrack, whole+t, eight1/2, ee, velocity);
                    addNote(solotrack, whole+eight1/2+t, eight2/2, ee, velocity);
                    addNote(solotrack, whole+eight1/2+eight2/2+t, eight2/2, ff, velocity);
                    addNote(solotrack, whole+eight1/2+eight2+t, eight1/2, gg, velocity);
                    addNote(solotrack, whole+quarter+t, eight1/2, ee, velocity);
                    addNote(solotrack, whole+quarter+eight1/2+t, eight2/2, ee, velocity);
                    addNote(solotrack, whole+quarter+eight1/2+eight2/2+t, eight2/2, ff, velocity);
                    addNote(solotrack, whole+quarter+eight1/2+eight2+t, eight1/2, gg, velocity);
                    addNote(solotrack, whole+half+t, eight1/2, ee, velocity);
                    addNote(solotrack, whole+half+eight1/2+t, eight2/2, ee, velocity);
                    addNote(solotrack, whole+half+eight1/2+eight2/2+t, eight2/2, ff, velocity);
                    addNote(solotrack, whole+half+eight1/2+eight2+t, eight1/2, gg, velocity);
                    addNote(solotrack, whole+half+quarter+t, eight1/2, ee, velocity);
                    addNote(solotrack, whole+half+quarter+eight1/2+t, eight2/2, ee, velocity);
                    addNote(solotrack, whole+half+quarter+eight1/2+eight2/2+t, eight2/2, ff, velocity);
                    addNote(solotrack, whole+half+quarter+eight1/2+eight2+t, eight1/2, gg, velocity);
                    }
                    else{
                      addNote(solotrack, whole+t, eight1, ee, velocity);
                      addNote(solotrack, whole+eight1+t, eight2, ff, velocity);
                      addNote(solotrack, whole+quarter+t, eight1, gg, velocity);
                      addNote(solotrack, whole+quarter+eight1+t, eight2+half, ff, velocity);
                    }
                  }
                  // NUMBER FOUR RIGHT HERE (3)
                  else if(rhythm<.20){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    p = Math.random();
                    if(gg-1>=low&&gg+1<=high){
                    if(p<.5) hh = gg+lastdir;
                    else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                    else hh = gg;}
                    else if(gg-1<low){hh = gg+1; lastdir = 1;}
                    else{hh = gg-1; lastdir = -1;}
                    p = Math.random();
                    if(hh-1>=low&&hh+1<=high){
                    if(p<.5) ii = hh+lastdir;
                    else if(p<.8){ii = hh-lastdir; lastdir = -lastdir;}
                    else ii = hh;}
                    else if(hh-1<low){ii = hh+1; lastdir = 1;}
                    else{ii = hh-1; lastdir = -1;}
                    p = Math.random();
                    if(ii-1>=low&&ii+1<=high){
                    if(p<.5) jj = ii+lastdir;
                    else if(p<.8){jj = ii-lastdir; lastdir = -lastdir;}
                    else jj = ii;}
                    else if(ii-1<low){jj = ii+1; lastdir = 1;}
                    else{jj = ii-1; lastdir = -1;}
                    p = Math.random();
                    if(jj-1>=low&&jj+1<=high){
                    if(p<.5) kk = jj+lastdir;
                    else if(p<.8){kk = jj-lastdir; lastdir = -lastdir;}
                    else kk = jj;}
                    else if(jj-1<low){kk = jj+1; lastdir = 1;}
                    else{kk = jj-1; lastdir = -1;}
                    
                    lastnote = kk;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig); ii = findBlues(ii, keysig);
                    jj = findBlues(jj, keysig); kk = findBlues(kk, keysig); 
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, eight1+t, eight2, aa, velocity);
                    addNote(solotrack, quarter+t, eight1, bb, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, cc, velocity);
                    addNote(solotrack, half+t, eight1, dd, velocity);
                    addNote(solotrack, half+eight1+t, eight2, ee, velocity);
                    addNote(solotrack, half+quarter+t, eight1, ff, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, gg, velocity);
                    addNote(solotrack, whole+t, eight1, hh, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, ii, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, jj, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, kk, velocity);
                  }
                  // NUMBER FIVE RIGHT HERE (4)
                  else if(rhythm<.25){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    
                    lastnote = gg;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, eight1+t, quarter+eight2, aa, velocity);
                    addNote(solotrack, half+t, eight1, bb, velocity);
                    addNote(solotrack, half+eight1+t, eight2, cc, velocity);
                    addNote(solotrack, whole+t, quarter, dd, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, ee, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, ff, velocity);
                    if(!(count+1==measures&&mcount==10))
                    addNote(solotrack, whole+half+eight1+t, eight2, ff, velocity);
                  }
                  // NUMBER SIX RIGHT HERE (5)
                  else if(rhythm<.30){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    
                    lastnote = ff;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, quarter+t, quarter, aa, velocity);
                    addNote(solotrack, half+t, eight1, bb, velocity);
                    addNote(solotrack, half+eight1+t, eight2, cc, velocity);
                    if(!(count+1==measures&&mcount==10)){
                    addNote(solotrack, whole+quarter+t, quarter, aa, velocity);
                    addNote(solotrack, whole+half+t, eight1, bb, velocity);
                    addNote(solotrack, whole+half+eight1+t, eight2, cc, velocity);
                    }
                    else{
                      addNote(solotrack, whole+eight1+t, quarter, bb, velocity);
                      addNote(solotrack, whole+quarter+eight1+t, eight2, bb, velocity);
                    }
                  }
                  // NUMBER SEVEN RIGHT HERE (6)
                  else if(rhythm<.35){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    cc = bb+lastdir;
                    dd = cc+lastdir;
                    ee = dd+lastdir;
                    ff = ee+lastdir;
                    gg = ff+lastdir;
                    hh = gg+lastdir;
                    
                    lastnote = bb;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, eight1, aa, velocity);
                    addNote(solotrack, eight1+t, eight2, bb, velocity);
                    addNote(solotrack, quarter+t, eight1, cc, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, half+t, eight1, ee, velocity);
                    addNote(solotrack, half+eight1+t, eight2, ff, velocity);
                    addNote(solotrack, half+quarter+t, eight1, gg, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, hh, velocity);
                    addNote(solotrack, whole+t, eight2/2, gg, velocity);
                    addNote(solotrack, whole+eight2/2+t, eight2/2, ff, velocity);
                    addNote(solotrack, whole+eight2+t, eight1/2, ee, velocity);
                    addNote(solotrack, whole+eight1/2+eight2+t, eight1/2, dd, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, cc, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, bb, velocity);
                  }
                  // NUMBER EIGHT RIGHT HERE (7)
                  else if(rhythm<.40){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                      }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    p = Math.random();
                    if(gg-1>=low&&gg+1<=high){
                    if(p<.5) hh = gg+lastdir;
                    else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                    else hh = gg;}
                    else if(gg-1<low){hh = gg+1; lastdir = 1;}
                    else{hh = gg-1; lastdir = -1;}
                    p = Math.random();
                    if(hh-1>=low&&hh+1<=high){
                    if(p<.5) ii = hh+lastdir;
                    else if(p<.8){ii = hh-lastdir; lastdir = -lastdir;}
                    else ii = hh;}
                    else if(hh-1<low){ii = hh+1; lastdir = 1;}
                    else{ii = hh-1; lastdir = -1;}
                    p = Math.random();
                    if(ii-1>=low&&ii+1<=high){
                    if(p<.5) jj = ii+lastdir;
                    else if(p<.8){jj = ii-lastdir; lastdir = -lastdir;}
                    else jj = ii;}
                    else if(ii-1<low){jj = ii+1; lastdir = 1;}
                    else{jj = ii-1; lastdir = -1;}
                    p = Math.random();
                    if(jj-1>=low&&jj+1<=high){
                    if(p<.5) kk = jj+lastdir;
                    else if(p<.8){kk = jj-lastdir; lastdir = -lastdir;}
                    else kk = jj;}
                    else if(jj-1<low){kk = jj+1; lastdir = 1;}
                    else{kk = jj-1; lastdir = -1;}
                    p = Math.random();
                    if(kk-1>=low&&kk+1<=high){
                    if(p<.5) ll = kk+lastdir;
                    else if(p<.8){ll = kk-lastdir; lastdir = -lastdir;}
                    else ll = kk;}
                    else if(kk-1<low){ll = kk+1; lastdir = 1;}
                    else{ll = kk-1; lastdir = -1;}
                    p = Math.random();
                    if(ll-1>=low&&ll+1<=high){
                    if(p<.5) mm = ll+lastdir;
                    else if(p<.8){mm = ll-lastdir; lastdir = -lastdir;}
                    else mm = ll;}
                    else if(ll-1<low){mm = ll+1; lastdir = 1;}
                    else{mm = ll-1; lastdir = -1;}
                    
                    lastnote = mm;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig); ii = findBlues(ii, keysig);
                    jj = findBlues(jj, keysig); kk = findBlues(kk, keysig); ll = findBlues(ll, keysig);
                    mm = findBlues(mm, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, eight1+t, quarter, aa, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, bb, velocity);
                    addNote(solotrack, half+t, eight1, cc, velocity);
                    addNote(solotrack, half+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, ee, velocity);
                    addNote(solotrack, whole+t, eight1/2, ff, velocity);
                    addNote(solotrack, whole+eight1/2+t, eight1/2, gg, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, hh, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, ii, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, jj, velocity);
                    if(!(count+1==measures&&mcount==10)){
                    addNote(solotrack, whole+half+eight1+t, eight2, kk, velocity);
                    addNote(solotrack, whole+half+quarter+t, eight1, ll, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, mm, velocity);
                    }
                  }
                  // NUMBER NINE RIGHT HERE (9)
                  else if(rhythm<.45){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    
                    lastnote = gg;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, quarter, aa, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, bb, velocity);
                    addNote(solotrack, half+t, eight1, cc, velocity);
                    addNote(solotrack, half+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, whole+t, eight1, ee, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, ff, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, half+eight2, gg, velocity);
                  }
                  // NUMBER TEN RIGHT HERE (10)
                  else if(rhythm<.50){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    p = Math.random();
                    if(gg-1>=low&&gg+1<=high){
                    if(p<.5) hh = gg+lastdir;
                    else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                    else hh = gg;}
                    else if(gg-1<low){hh = gg+1; lastdir = 1;}
                    else{hh = gg-1; lastdir = -1;}
                    
                    lastnote = hh;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, eight1/2, aa, velocity);
                    addNote(solotrack, eight1/2+t, eight2/2, bb, velocity);
                    addNote(solotrack, eight1/2+eight2/2+t, eight2/2, cc, velocity);
                    addNote(solotrack, eight1/2+eight2+t, eight1/2, dd, velocity);
                    addNote(solotrack, quarter+t, eight1/2, aa, velocity);
                    addNote(solotrack, quarter+eight1/2+t, eight2/2, bb, velocity);
                    addNote(solotrack, quarter+eight1/2+eight2/2+t, eight2/2, cc, velocity);
                    addNote(solotrack, quarter+eight1/2+eight2+t, eight1/2, dd, velocity);
                    addNote(solotrack, half+t, eight1/2, aa, velocity);
                    addNote(solotrack, half+eight1/2+t, eight2/2, bb, velocity);
                    addNote(solotrack, half+eight1/2+eight2/2+t, eight2/2, cc, velocity);
                    addNote(solotrack, half+eight1/2+eight2+t, eight1/2, dd, velocity);
                    addNote(solotrack, half+quarter+t, eight1/2, aa, velocity);
                    addNote(solotrack, half+quarter+eight1/2+t, eight2/2, bb, velocity);
                    addNote(solotrack, half+quarter+eight1/2+eight2/2+t, eight2/2, cc, velocity);
                    addNote(solotrack, half+quarter+eight1/2+eight2+t, eight1/2, dd, velocity);
                    addNote(solotrack, whole+t, eight1, ee, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, ff, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, gg, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, hh, velocity);
                  }
                  // NUMBER ELEVEN RIGHT HERE (11)
                  else if(rhythm<.55){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    
                    lastnote = dd;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, eight1+t, quarter, aa, velocity);
                    addNote(solotrack, quarter+eight1+t, quarter, bb, velocity);
                    addNote(solotrack, half+eight1+t, quarter, cc, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, dd, velocity);
                    if(tempo<=80)addNote(solotrack, whole+eight1/2+t, eight1/2, dd, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, dd, velocity);
                    if(tempo<=80)addNote(solotrack, whole+quarter+eight1/2+t, eight1/2, dd, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, dd, velocity);
                    if(tempo<=80)addNote(solotrack, whole+half+eight1/2+t, eight1/2, dd, velocity);
                    addNote(solotrack, whole+half+eight1+t, eight2, dd, velocity);
                    if(tempo<=80)addNote(solotrack, whole+half+quarter+eight1/2+t, eight1/2, dd, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, dd, velocity);
                  }
                  // NUMBER TWELVE RIGHT HERE (12)
                  else if(rhythm<.60){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                      }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    p = Math.random();
                    if(gg-1>=low&&gg+1<=high){
                    if(p<.5) hh = gg+lastdir;
                    else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                    else hh = gg;}
                    else if(gg-1<low){hh = gg+1; lastdir = 1;}
                    else{hh = gg-1; lastdir = -1;}
                    p = Math.random();
                    if(hh-1>=low&&hh+1<=high){
                    if(p<.5) ii = hh+lastdir;
                    else if(p<.8){ii = hh-lastdir; lastdir = -lastdir;}
                    else ii = hh;}
                    else if(hh-1<low){ii = hh+1; lastdir = 1;}
                    else{ii = hh-1; lastdir = -1;}
                    p = Math.random();
                    if(ii-1>=low&&ii+1<=high){
                    if(p<.5) jj = ii+lastdir;
                    else if(p<.8){jj = ii-lastdir; lastdir = -lastdir;}
                    else jj = ii;}
                    else if(ii-1<low){jj = ii+1; lastdir = 1;}
                    else{jj = ii-1; lastdir = -1;}
                    p = Math.random();
                    if(jj-1>=low&&jj+1<=high){
                    if(p<.5) kk = jj+lastdir;
                    else if(p<.8){kk = jj-lastdir; lastdir = -lastdir;}
                    else kk = jj;}
                    else if(jj-1<low){kk = jj+1; lastdir = 1;}
                    else{kk = jj-1; lastdir = -1;}
                    
                    lastnote = kk;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig); ii = findBlues(ii, keysig);
                    jj = findBlues(jj, keysig); kk = findBlues(kk, keysig); /*ll = findBlues(ll, keysig);
                    mm = findBlues(mm, keysig); nn = findBlues(nn, keysig); oo = findBlues(oo, keysig);*/
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, quarter, aa, velocity);
                    addNote(solotrack, quarter+t, quarter, bb, velocity);
                    addNote(solotrack, half+t, eight1, cc, velocity);
                    addNote(solotrack, half+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, ee, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, ff, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, gg, velocity);
                    addNote(solotrack, whole+half+t, eight1, hh, velocity);
                    addNote(solotrack, whole+half+eight1+t, eight2, ii, velocity);
                    addNote(solotrack, whole+half+quarter+t, eight1, jj, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, kk, velocity);
                    /*addNote(solotrack, whole+straighteight+t, straighteight, ff, velocity);
                    addNote(solotrack, whole+quarter+straighteight+t, straighteight, gg, velocity);
                    addNote(solotrack, whole+half+t, ssixteen, hh, velocity);
                    addNote(solotrack, whole+half+ssixteen+t, ssixteen, ii, velocity);
                    addNote(solotrack, whole+half+straighteight+t, ssixteen, jj, velocity);
                    addNote(solotrack, whole+half+straighteight+ssixteen+t, ssixteen, kk, velocity);
                    addNote(solotrack, whole+half+quarter+t, ssixteen, ll, velocity);
                    addNote(solotrack, whole+half+quarter+ssixteen+t, ssixteen, mm, velocity);
                    addNote(solotrack, whole+half+quarter+straighteight+t, ssixteen, nn, velocity);
                    addNote(solotrack, whole+half+quarter+straighteight+ssixteen+t, ssixteen, oo, velocity);*/
                  }
                  // NUMBER THIRTEEN RIGHT HERE (13)
                  else if(rhythm<.65){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    
                    lastnote = gg;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, whole, aa, velocity);
                    if(tempo<=80)addNote(solotrack, whole+eight1/2+t, eight1/2, bb, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, cc, velocity);
                    if(tempo<=80)addNote(solotrack, whole+quarter+eight1/2+t, eight1/2, bb, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, cc, velocity);
                    addNote(solotrack, whole+half+t, eight1, dd, velocity);
                    addNote(solotrack, whole+half+eight1+t, eight2, ee, velocity);
                    addNote(solotrack, whole+half+quarter+t, eight1, ff, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, gg, velocity);
                  }
                  // NUMBER FOURTEEN RIGHT HERE (14)
                  else if(rhythm<.70){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    
                    lastnote = ee;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, half, aa, velocity);
                    addNote(solotrack, half+t, half, bb, velocity);
                    addNote(solotrack, whole+t, half, cc, velocity);
                    addNote(solotrack, whole+half+t, quarter, dd, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, ee, velocity);
                  }
                  // NUMBER FIFTEEN RIGHT HERE (15)
                  else if(rhythm<.75){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                      }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    p = Math.random();
                    if(gg-1>=low&&gg+1<=high){
                    if(p<.5) hh = gg+lastdir;
                    else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                    else hh = gg;}
                    else if(gg-1<low){hh = gg+1; lastdir = 1;}
                    else{hh = gg-1; lastdir = -1;}
                    p = Math.random();
                    if(hh-1>=low&&hh+1<=high){
                    if(p<.5) ii = hh+lastdir;
                    else if(p<.8){ii = hh-lastdir; lastdir = -lastdir;}
                    else ii = hh;}
                    else if(hh-1<low){ii = hh+1; lastdir = 1;}
                    else{ii = hh-1; lastdir = -1;}
                    p = Math.random();
                    if(ii-1>=low&&ii+1<=high){
                    if(p<.5) jj = ii+lastdir;
                    else if(p<.8){jj = ii-lastdir; lastdir = -lastdir;}
                    else jj = ii;}
                    else if(ii-1<low){jj = ii+1; lastdir = 1;}
                    else{jj = ii-1; lastdir = -1;}
                    
                    lastnote = jj;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig); ii = findBlues(ii, keysig);
                    jj = findBlues(jj, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, quarter, aa, velocity);
                    addNote(solotrack, quarter+t, quarter, bb, velocity);
                    addNote(solotrack, half+t, eight1, cc, velocity);
                    addNote(solotrack, half+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, half+quarter+t, eight1, ee, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, ff, velocity);
                    addNote(solotrack, whole+t, eight1, gg, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, hh, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, ii, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, jj, velocity);
                  }
                  // NUMBER SIXTEEN RIGHT HERE (18)
                  else if(rhythm<.80){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                      }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = ee+lastdir;
                    else if(p<.8){ff = ee-lastdir; lastdir = -lastdir;}
                    else ff = ee;}
                    else if(ee-1<low){ff = ee+1; lastdir = 1;}
                    else{ff = ee-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    p = Math.random();
                    if(gg-1>=low&&gg+1<=high){
                    if(p<.5) hh = gg+lastdir;
                    else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                    else hh = gg;}
                    else if(gg-1<low){hh = gg+1; lastdir = 1;}
                    else{hh = gg-1; lastdir = -1;}
                    p = Math.random();
                    if(hh-1>=low&&hh+1<=high){
                    if(p<.5) ii = hh+lastdir;
                    else if(p<.8){ii = hh-lastdir; lastdir = -lastdir;}
                    else ii = hh;}
                    else if(hh-1<low){ii = hh+1; lastdir = 1;}
                    else{ii = hh-1; lastdir = -1;}
                    p = Math.random();
                    if(ii-1>=low&&ii+1<=high){
                    if(p<.5) jj = ii+lastdir;
                    else if(p<.8){jj = ii-lastdir; lastdir = -lastdir;}
                    else jj = ii;}
                    else if(ii-1<low){jj = ii+1; lastdir = 1;}
                    else{jj = ii-1; lastdir = -1;}
                    
                    lastnote = jj;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig); ii = findBlues(ii, keysig);
                    jj = findBlues(jj, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, quarter+t, quarter, aa, velocity);
                    addNote(solotrack, half+t, eight1, bb, velocity);
                    addNote(solotrack, half+eight1+t, eight2, cc, velocity);
                    addNote(solotrack, half+quarter+eight1+t, quarter, dd, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, ee, velocity);
                    addNote(solotrack, whole+quarter+t, eight1, ff, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, gg, velocity);
                    addNote(solotrack, whole+half+t, eight1, hh, velocity);
                    addNote(solotrack, whole+half+eight1+t, eight2, ii, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, hh, velocity);
                  }
                  // NUMBER SEVENTEEN RIGHT HERE (19)
                  else if(rhythm<.85){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    
                    lastnote = ee;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, quarter+t, quarter, aa, velocity);
                    addNote(solotrack, half+t, half, bb, velocity);
                    addNote(solotrack, whole+t, quarter+eight1, cc, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, ee, velocity);
                  }
                  // NUMBER EIGHTEEN RIGHT HERE (20)
                  else if(rhythm<.90){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                    }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    
                    lastnote = dd;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, 0+t, eight1, aa, velocity);
                    addNote(solotrack, eight1+t, eight2, aa, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, aa, velocity);
                    addNote(solotrack, half+t, eight1, bb, velocity);
                    addNote(solotrack, half+eight1+t, eight2, bb, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, bb, velocity);
                    addNote(solotrack, whole+t, eight1, cc, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, cc, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, cc, velocity);
                    addNote(solotrack, whole+half+t, eight1, dd, velocity);
                    addNote(solotrack, whole+half+eight1+t, eight2, dd, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, dd, velocity);
                  }
                  // NUMBER NINETEEN RIGHT HERE (22)
                  else if(rhythm<.95){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                      }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = dd+lastdir;
                    else if(p<.8){ff = dd-lastdir; lastdir = -lastdir;}
                    else ff = dd;}
                    else if(dd-1<low){ff = dd+1; lastdir = 1;}
                    else{ff = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    
                    lastnote = gg;
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    addNote(solotrack, eight1+t, eight2, aa, velocity);
                    addNote(solotrack, quarter+t, eight1, bb, velocity);
                    addNote(solotrack, quarter+eight1+t, eight2, cc, velocity);
                    if(tempo<=80)addNote(solotrack, half+eight1/2+t, eight1/2, dd, velocity);
                    addNote(solotrack, half+eight1+t, eight2, ee, velocity);
                    if(tempo<=80)addNote(solotrack, half+quarter+eight1/2+t, eight1/2, dd, velocity);
                    addNote(solotrack, half+quarter+eight1+t, eight2, ee, velocity);
                    if(tempo<=80)addNote(solotrack, whole+eight1/2+t, eight1/2, ff, velocity);
                    addNote(solotrack, whole+eight1+t, eight2, gg, velocity);
                    if(tempo<=80)addNote(solotrack, whole+quarter+eight1/2+t, eight1/2, ff, velocity);
                    addNote(solotrack, whole+quarter+eight1+t, eight2, gg, velocity);
                    if(tempo<=80)addNote(solotrack, whole+half+eight1/2+t, eight1/2, ff, velocity);
                    addNote(solotrack, whole+half+eight1+t, eight2, gg, velocity);
                    if(tempo<=80)addNote(solotrack, whole+half+quarter+eight1/2+t, eight1/2, ff, velocity);
                    addNote(solotrack, whole+half+quarter+eight1+t, eight2, gg, velocity);
                  }
                  // NUMBER TWENTY RIGHT HERE (23)
                  else if(rhythm<1.0){
                    if(mcount == 0){
                      if(Math.random()<.5)
                        aa = 0;
                      else
                        aa = 6;
                    }
                    else{
                      p = Math.random();
                      if(lastnote-1>=low&&lastnote+1<=high){
                      if(p<.5) aa = lastnote+lastdir;
                      else if(p<.8){aa = lastnote-lastdir; lastdir = -lastdir;}
                      else aa = lastnote;}
                      else if(lastnote-1<low){aa = lastnote+1; lastdir = 1;}
                      else{aa = lastnote-1; lastdir = -1;}
                      }
                    p = Math.random();
                    if(aa-1>=low&&aa+1<=high){
                    if(p<.5) bb = aa+lastdir;
                    else if(p<.8){bb = aa-lastdir; lastdir = -lastdir;}
                    else bb = aa;}
                    else if(aa-1<low){bb = aa+1; lastdir = 1;}
                    else{bb = aa-1; lastdir = -1;}
                    p = Math.random();
                    if(bb-1>=low&&bb+1<=high){
                    if(p<.5) cc = bb+lastdir;
                    else if(p<.8){cc = bb-lastdir; lastdir = -lastdir;}
                    else cc = bb;}
                    else if(bb-1<low){cc = bb+1; lastdir = 1;}
                    else{cc = bb-1; lastdir = -1;}
                    p = Math.random();
                    if(cc-1>=low&&cc+1<=high){
                    if(p<.5) dd = cc+lastdir;
                    else if(p<.8){dd = cc-lastdir; lastdir = -lastdir;}
                    else dd = cc;}
                    else if(cc-1<low){dd = cc+1; lastdir = 1;}
                    else{dd = cc-1; lastdir = -1;}
                    p = Math.random();
                    if(dd-1>=low&&dd+1<=high){
                    if(p<.5) ee = dd+lastdir;
                    else if(p<.8){ee = dd-lastdir; lastdir = -lastdir;}
                    else ee = dd;}
                    else if(dd-1<low){ee = dd+1; lastdir = 1;}
                    else{ee = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ee-1>=low&&ee+1<=high){
                    if(p<.5) ff = dd+lastdir;
                    else if(p<.8){ff = dd-lastdir; lastdir = -lastdir;}
                    else ff = dd;}
                    else if(dd-1<low){ff = dd+1; lastdir = 1;}
                    else{ff = dd-1; lastdir = -1;}
                    p = Math.random();
                    if(ff-1>=low&&ff+1<=high){
                    if(p<.5) gg = ff+lastdir;
                    else if(p<.8){gg = ff-lastdir; lastdir = -lastdir;}
                    else gg = ff;}
                    else if(ff-1<low){gg = ff+1; lastdir = 1;}
                    else{gg = ff-1; lastdir = -1;}
                    if(tempo<=100){
                      hh = gg-1;
                      ii = gg-2;
                      jj = 0;
                      lastnote = ii;
                    }
                    else{
                      p = Math.random();
                      if(gg-1>=low&&gg+1<=high){
                      if(p<.5) hh = gg+lastdir;
                      else if(p<.8){hh = gg-lastdir; lastdir = -lastdir;}
                      else hh = gg;}
                      else if(gg-1<low){hh = gg+1; lastdir = 1;}
                      else{hh = gg-1; lastdir = -1;}
                      p = Math.random();
                      if(hh-1>=low&&hh+1<=high){
                      if(p<.5) ii = hh+lastdir;
                      else if(p<.8){ii = hh-lastdir; lastdir = -lastdir;}
                      else ii = hh;}
                      else if(hh-1<low){ii = hh+1; lastdir = 1;}
                      else{ii = hh-1; lastdir = -1;}
                      p = Math.random();
                      if(ii-1>=low&&ii+1<=high){
                      if(p<.5) jj = ii+lastdir;
                      else if(p<.8){jj = ii-lastdir; lastdir = -lastdir;}
                      else jj = ii;}
                      else if(ii-1<low){jj = ii+1; lastdir = 1;}
                      else{jj = ii-1; lastdir = -1;}
                      lastnote = jj;
                    }
                    
                    aa = findBlues(aa, keysig); bb = findBlues(bb, keysig); cc = findBlues(cc, keysig);
                    dd = findBlues(dd, keysig); ee = findBlues(ee, keysig); ff = findBlues(aa, keysig);
                    gg = findBlues(gg, keysig); hh = findBlues(hh, keysig); ii = findBlues(ii, keysig);
                    if(tempo>100) jj = findBlues(jj, keysig);
                      
                    //addNote(solotrack, startTick+t, tickLength, key, velocity);
                    if(tempo<=100){
                      addNote(solotrack, quarter+t, quarter, aa, velocity);
                      addNote(solotrack, half+t, eight1, bb, velocity);
                      addNote(solotrack, half+eight1+t, quarter, cc, velocity);
                      addNote(solotrack, half+quarter+eight1+t, eight2, dd, velocity);
                      addNote(solotrack, whole+eight1+t, eight2, ee, velocity);
                      addNote(solotrack, whole+quarter+t, eight1, ff, velocity);
                      addNote(solotrack, whole+quarter+eight1+t, 3, gg, velocity);
                      addNote(solotrack, whole+quarter+eight1+3+t, 3, hh, velocity);
                      addNote(solotrack, whole+quarter+eight1+6+t, 4, ii, velocity);
                      addNote(solotrack, whole+half+t, 3, gg, velocity);
                      addNote(solotrack, whole+half+3+t, 4, hh, velocity);
                      addNote(solotrack, whole+half+7+t, 4, ii, velocity);
                      addNote(solotrack, whole+half+eight1/2+t, 3, gg, velocity);
                      addNote(solotrack, whole+half+eight1/2+3+t, 4, hh, velocity);
                      addNote(solotrack, whole+half+eight1/2+7+t, 4, ii, velocity);
                      addNote(solotrack, whole+half+eight1+t, 3, gg, velocity);
                      addNote(solotrack, whole+half+eight1+3+t, 3, hh, velocity);
                      addNote(solotrack, whole+half+eight1+6+t, 4, ii, velocity);
                      addNote(solotrack, whole+half+quarter+t, 3, gg, velocity);
                      addNote(solotrack, whole+half+quarter+3+t, 4, hh, velocity);
                      addNote(solotrack, whole+half+quarter+7+t, 4, ii, velocity);
                      addNote(solotrack, whole+half+quarter+eight1/2+t, 3, gg, velocity);
                      addNote(solotrack, whole+half+quarter+eight1/2+3+t, 4, hh, velocity);
                      addNote(solotrack, whole+half+quarter+eight1/2+7+t, 4, ii, velocity);
                      addNote(solotrack, whole+half+quarter+eight1+t, 3, gg, velocity);
                      addNote(solotrack, whole+half+quarter+eight1+3+t, 3, hh, velocity);
                      addNote(solotrack, whole+half+quarter+eight1+6+t, 4, ii, velocity);
                    }
                    else{
                      addNote(solotrack, eight1+t, quarter, aa, velocity);
                      addNote(solotrack, quarter+eight1+t, eight2, bb, velocity);
                      addNote(solotrack, half+t, eight1, cc, velocity);
                      addNote(solotrack, half+eight1+t, quarter, dd, velocity);
                      addNote(solotrack, half+quarter+eight1+t, eight2, ee, velocity);
                      addNote(solotrack, whole+eight1/2+t, eight1/2, ff, velocity);
                      addNote(solotrack, whole+eight1+t, eight2, gg, velocity);
                      addNote(solotrack, whole+quarter+t, eight1/2, hh, velocity);
                      addNote(solotrack, whole+quarter+eight1/2+t, eight1/2, ii, velocity);
                      addNote(solotrack, whole+quarter+eight1+t, eight2+quarter, jj, velocity);
                    }
                  }
                }
                }
                t+=twhole;
            }
        }
    }

    public static void addNote(Track track, int startTick,
            int tickLength, int key, int velocity)
            throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, 2, key, velocity);
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, 2, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }
    
    public static void addNote1(Track track, int startTick,
            int tickLength, int key, int velocity)
            throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, 0, key, velocity);
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, 0, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }
    
    public static void addNote2(Track track, int startTick,
            int tickLength, int key, int velocity)
            throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, 1, key, velocity);
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, 1, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }

    public static void addChords(double number, Track track, int startTick, int first,
            int second, int third, int velocity)
            throws InvalidMidiDataException {
        if (number < .25 && startTick >= 128) {
            addNote2(track, startTick - 10, 128, first, velocity);
            addNote2(track, startTick - 10, 128, second, velocity);
            addNote2(track, startTick - 10, 128, third, velocity);
        } else if (number < .5) {
            addNote2(track, startTick + 22, 96, first, velocity);
            addNote2(track, startTick + 22, 96, second, velocity);
            addNote2(track, startTick + 22, 96, third, velocity);
        }
        else if (number < .75) {
            addNote2(track, startTick + 22, 10, first, velocity);
            addNote2(track, startTick + 22, 10, second, velocity);
            addNote2(track, startTick + 22, 10, third, velocity);
            addNote2(track, startTick + 54, 64, first, velocity);
            addNote2(track, startTick + 54, 64, second, velocity);
            addNote2(track, startTick + 54, 64, third, velocity);
        } else {
            addNote2(track, startTick, 118, first, velocity);
            addNote2(track, startTick, 118, second, velocity);
            addNote2(track, startTick, 118, third, velocity);
        }
    }

    private static int findBlues(int num, String keysig) {
      
        int x;
        if (keysig.equals("C")) x = 0;
        else if (keysig.equals("Db")) x = 1;
        else if (keysig.equals("D")) x = 2;
        else if (keysig.equals("Eb")) x = 3;
        else if (keysig.equals("E")) x = 4;
        else if (keysig.equals("F")) x = 5;
        else if (keysig.equals("Gb")) x = 6;
        else if (keysig.equals("G")) x = 7;
        else if (keysig.equals("Ab")) x = 8;
        else if (keysig.equals("A")) x = 9;
        else if (keysig.equals("Bb")) x = 10;
        else if (keysig.equals("B")) x = 11;
        else x = 0;
      
        int note = 0;

        switch (num) {
            case -12:
                note = 36+x; // C
                break;
            case -11:
                note = 39+x; // E Flat
                break;
            case -10:
                note = 41+x; // F
                break;
            case -9:
                note = 42+x; // F Sharp/G Flat
                break;
            case -8:
                note = 43+x; // G
                break;
            case -7:
                note = 46+x; // B Flat
                break;
            case -6:
                note = 48+x; // C
                break;
            case -5:
                note = 51+x; // E Flat
                break;
            case -4:
                note = 53+x; // F
                break;
            case -3:
                note = 54+x; // F Sharp/G Flat
                break;
            case -2:
                note = 55+x; // G
                break;
            case -1:
                note = 58+x; // B Flat
                break;
            case 0:
                note = 60+x; // C
                break;
            case 1:
                note = 63+x; // E Flat
                break;
            case 2:
                note = 65+x; // F
                break;
            case 3:
                note = 66+x;  // F Sharp/G Flat
                break;
            case 4:
                note = 67+x; // G
                break;
            case 5:
                note = 70+x; // B Flat
                break;
            case 6:
                note = 72+x; // C
                break;
            case 7:
                note = 75+x; // E Flat
                break;
            case 8:
                note = 77+x; // F
                break;
            case 9:
                note = 78+x; // F Sharp/G Flat
                break;
            case 10:
                note = 79+x; // G
                break;
            case 11:
                note = 82+x; // B Flat
                break;
            case 12:
                note = 84+x; // C
                break;
            case 13:
                note = 87+x; // E Flat
                break;
            case 14:
                note = 89+x; // F
                break;
            case 15:
                note = 90+x; // F Sharp/G Flat
                break;
            case 16:
                note = 91+x; // G
                break;
            case 17:
                note = 94+x; // B Flat
                break;
            case 18:
                note = 96+x; // C
                break;
            default:
                note = 0;
                break;
        };

        return note;
    }
}