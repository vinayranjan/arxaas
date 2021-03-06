package no.nav.arxaas.hierarchy;

import org.deidentifier.arx.aggregates.HierarchyBuilderGroupingBased;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Level {


    private final int level;
    @NotNull
    private final List<Group> groups;

    public int getLevel() {
        return level;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Level(int level, List<Group> groups) {
        this.level = level;
        this.groups = groups;
    }

    public void applyTo(HierarchyBuilderGroupingBased builder) {
        for (Level.Group group: getGroups()){
            if(group.getLabel() != null) {
                builder.getLevel(getLevel()).addGroup(group.getGrouping(), group.getLabel());
            }
            else {
                builder.getLevel(getLevel()).addGroup(group.getGrouping());
            }
        }
    }

    public static class Group {
        private final int grouping;
        private final String label;

        public Group(int grouping, String label) {
            this.grouping = grouping;
            this.label = label;
        }

        public Group(int grouping) {
            this.grouping = grouping;
            this.label = null;
        }

        public int getGrouping() {
            return grouping;
        }

        public String getLabel() {
            return label;
        }
    }
}

