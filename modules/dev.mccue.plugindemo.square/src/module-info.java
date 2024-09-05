import dev.mccue.plugindemo.api.Entity;
import dev.mccue.plugindemo.square.SquareEntity;

module dev.mccue.plugindemo.square {
    requires dev.mccue.plugindemo.api;
    requires com.google.common;

    provides Entity with SquareEntity;
}