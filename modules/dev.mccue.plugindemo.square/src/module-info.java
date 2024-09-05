import dev.mccue.plugindemo.api.Entity;
import dev.mccue.plugindemo.square.SquareEntity;

module dev.mccue.plugindemo.square {
    requires dev.mccue.plugindemo.api;
    requires com.google.common;
    requires animal.sniffer.annotations;

    provides Entity with SquareEntity;
}