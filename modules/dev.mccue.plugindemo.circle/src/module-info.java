import dev.mccue.plugindemo.api.Entity;
import dev.mccue.plugindemo.circle.CircleEntity;

module dev.mccue.plugindemo.circle {
    requires dev.mccue.plugindemo.api;

    provides Entity with CircleEntity;
}