import dev.mccue.plugindemo.api.Entity;
import dev.mccue.plugindemo.circle.CircleEntity;

module dev.mccue.plugindemo.circle {
    requires dev.mccue.plugindemo.api;
    requires com.google.common;
    requires org.checkerframework.checker.qual;

    provides Entity with CircleEntity;
}