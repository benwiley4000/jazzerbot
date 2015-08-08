# jazzerbot
User-Customized Jazz Improvisation Generation

JazzerBot is a program written in Java that was originally created for a high school science competition. It gives the user tools to generate simple, improvised jazz solos over a standard blues progression.

JazzerBot has the following features as of v2.35:

•  The ability to choose a key in which a solo will be played
•  The ability to choose a tempo in which a solo will be played
•  The ability to choose between different jazz-oriented instruments
•  The ability to choose the length a solo will last (currently in 12 measure increments)
•  The ability to toggle a bass line track on or off
•  The ability to toggle a chord accompaniment track on or off
•  The ability to toggle the solo itself on or off
•  The ability to toggle an alternate last-measure bass line ending on or off
•  The ability to combine differently formatted pieces together to form a complex solo arrangement
•  The ability to port the produced sequence to an external MIDI device
•  The ability to save, load, and play generated solo sequences
•  A simple dialog-based graphical interface.

How to Use:

While it can be a little confusing, JazzerBot's rather simple to use after you get used to it. You'll first be prompted to choose whether to create a new solo or to open a previously created solo from a midi sequence file. As you'll figure out later, you can, indeed take solos you make in JazzerBot and replay them using a convenient function built into the program.

Assuming you opt not to replay an item but to create a new solo, you'll be first asked to choose a tempo that will encompass the entire sequence you produce. Next, you'll be asked to give parameters that will just apply to a certain section of the music. If you so wish, these too will encompass the entire piece of music you create; however, JazzerBot gives you the ability to add additions with alterations in musical characteristics so that you may create more complex improvised compositions. The selection of all these aspects is fairly straightforward-- you will use dropdowns, yes-or-no selections, and occasional fill-in-the-blanks to customize the music to your likings.

You'll be asked if you wish to trade fours with JazzerBot. If you can solo yourself and wish to interact with JazzerBot, go ahead. If not, it's highly advisable that you say no.

After being prompted to select which tracks you want to be included in the playback, the application will ask if you wish to port the audio to an external MIDI device. If you don't know what this is or you know none is attached, do not select this option-- doing so will mean you will either be forced into selecting an option that does not produce sound for your ears, or that you hit cancel and in doing so end the program, rendering your long, hard efforts, to prepare a workable jazz solo, useless.

If you do have such a device attached and wish to use it, select this option. Due to the workings of the system, displaying each option in a dropdown is very difficult to do without limitations. Instead, you will see numbers corresponding to a list of connected MIDI devices. Entering the correct number for your desired device will do the trick.

Finally, saving to a file. JazzerBot saves files as .MIDs, and it will save it as such whether you have the ".mid" extension or not. Just a notice-- attempts to save under alternative file types will fail, as will extensions that have started but not finished-- "testtest.mi" will not return what you want. 
