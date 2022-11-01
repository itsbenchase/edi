# Contributing - but for trusted friends

Okay, so you got the inside access. (If you're not supposed to be here - I'll know.) You all have a slightly different guide than the [standard contribution guide](https://edi.benchase.info/contribute.html), but that's mostly due to the additional setup here.

Since this inside access is invite-only, just keep the GitHub link a secret. It's not linked anywhere right now, so don't go spreading it.

Let's get into it!

Fair warning - There is some technical knowledge needed for this - specifically the ability to install things and use the command line.

## Part 1: Setup

First things first - the calculator was coded in Java (the only programming language I know), so you'll need to download it. You can download it for free for any operating system on Oracle's website: https://www.oracle.com/java/technologies/downloads/

Next - create a GitHub account if you don't have one (it's also free), and clone the database to your computer - I recommend using both [Github's offical CLI](https://cli.github.com/) and [Git](https://git-scm.com/) for all work with the EDI calculator (both programs are free).

Please register your GitHub username through [this short form](https://forms.gle/Z2DAJkB8Yk5yV6W5A), so I know who should actually be using this.

With that, you are all set up and ready to calculate some EDIs!

## Part 2: Calculation

This is the same process as outlined in the online contribution guide, but do not follow Part 3 there, you have a different process that does not require submitting stop IDs.

Any questions about the EDI rules (especially rules 3 and 4) - do not calculate before asking me about it. I'll probably end up calculating the route myself.

Run `java ediCalc` to run the calculator. When prompted to save line, enter `yes`, and when prompted to export, enter anything other than `yes`. If a route is the first route for an agency, please ignore the errors. It works as intended.

## Part 3: Adding to the database

Once a route is calculated and saved, run `java routeList` afterwards as this generates the `routes.html` page.

After you finish your EDI calculating, it's time to get it added to the database. You'll have to commit and push your Git changes (yeah that sounds complicated). I do it in one simple Git command (you'll be able to do this) from the command line - `git add * && git commit -m "[put whatever here i don't care this is just a message]" && git push`. That will do everything in one go.

Note that if you are actually knowledgeable with Git or the CLI (which I am not), there may be a better way to do this process. This is just the way I'd do it with my current knowledge.

Download and intall instructions for [the CLI](https://github.com/cli/cli#installation) and [Git](https://github.com/cli/cli#installation) are available.

## Some other documentation

You'll notice that there are a number of other programs included that support the EDI calculator. Quick rundown of what they do - but you won't need to use these. (Do not delete anything from your computer, it will mess everything up.)

`addStop.java` - adds a stop to the agency's stop listing, different than the -1 code in the calculator as you can add a stop here and use it over and over.

`list.java` - similar to `routeList`, but generates the pages with all of the stops.

`Stop.java` - general class defining what a stop has (name, id, lat, lon)