import dev.mccue.plugindemo.api.Entity;

module dev.mccue.plugindemo.game {
    requires java.desktop;
    requires dev.mccue.plugindemo.api;

    uses Entity;
}