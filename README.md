Around half a year ago I asked the crowd how they thought OSGi would be designed if it was made today.

One of my takeaways from that was that the majority OSGi's unique benefits could be accomplished by simply rolling
your own plugin system and that doing so wouldn't be that hard.

While I experimented with this locally, I think could be value in 
showing how that could be done.

## Concept

The basic idea of this plugin system is that we separate out an `api` module.
Plugins would compile against the interface exposed by this module and provide
implementations of interfaces for a module actually running the system to
dynamically find and consume.

While this structure works for any sort of modular application, the part
that makes it a plugin system is loading those modules from the filesystem at
runtime.

To accomplish that I used [ModuleLayer](https://docs.oracle.com/javase/22/docs/api/java/lang/ModuleLayer.html)s.
You can make a module layer per plugin or for a group of plugins then turn around
and use that, along with service provider declarations, to load in implementations 
of those interfaces.

The relevant code for the dynamic loading is under `modules/dev.mccue.plugindemo.game`
and the service provider declarations are in the module infos of each module.

My hope is that this can serve as a useful starting point for anyone looking
to add supported plugin functionality to their website, game, app, or whatever.

## Run the Code

To test this out first run

```bash 
java @bootstrap
```

then 

```bash 
java @project run
```

You can see other available commands by running `java @project` with no arguments
and the code for the build scripts is under `scripts/src`.