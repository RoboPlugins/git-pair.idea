# Git Pair

Plugin for the Idea platform ([IntelliJ](https://www.jetbrains.com/idea/), [AppCode](https://www.jetbrains.com/objc/), [Android Studio](http://developer.android.com/tools/studio/index.html)) that helps you do pair programming.

Like [Pivotal Lab's Git Scripts](https://github.com/pivotal/git_scripts), but for the best IDE ever built.

On my teams we often ask "Wait, who's paired right now?"  It's a bit inconvienient to find that information.  This plugin shows on screen who is paired, and allows you to change the pair.  Modeled after the git4idea plugin which allows you to see the branch you are on and quickly switch branches.

### Why not just use the command line?

Because the current `git pair` is not visible on the screen at a glance.  And typing `git pair` will reset your pair to nothing.

You could use the [pivotal scripts](https://github.com/pivotal/git_scripts) since this plugin will not work in XCode or Visual Studio.  On the other hand, there's always [AppCode](https://www.jetbrains.com/objc/) and [CLion](https://www.jetbrains.com/clion/)... (I can haz free licences jetbrains?  For testing my plugin?)

### Why not just do a pull-request on the git4idea plugin?

Because `git pair` is a not in core git, it's a plugin for git.  Not everyone that uses git, also does pair programming.
