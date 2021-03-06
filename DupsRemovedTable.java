import java.util.ArrayList;

/**
 * DupsRemovedTable - this class implements the duplicate removal operation of a
 * relational DB
 */

public class DupsRemovedTable extends Table {

    Table tab_dups_removed_from;

    /**
     * @param t - the table from which duplicates are to be removed
     */
    public DupsRemovedTable(Table t) {

        super("Removing duplicates from " + t.toString());
        tab_dups_removed_from = t;

        attr_names = t.attr_names;
        attr_types = t.attr_types;
    }

    public Table[] my_children() {
        return new Table[]{tab_dups_removed_from};
    }

    public Table optimize() {
        if(tab_dups_removed_from instanceof JoinTable) {
            JoinTable joinTab = (JoinTable) tab_dups_removed_from;

            DupsRemovedTable dupRemovedTab1 = new DupsRemovedTable(joinTab.first_join_tab);
            DupsRemovedTable dupRemovedTab2 = new DupsRemovedTable(joinTab.second_join_tab);

            return new JoinTable(dupRemovedTab1, dupRemovedTab2, joinTab.joinCondition);
        } else {
            return this;
        }
    }

    public ArrayList<Tuple> evaluate() {
        ArrayList<Tuple> toReturn = new ArrayList<>();
        for(Tuple t : tab_dups_removed_from.evaluate()){
            if(!toReturn.contains(t)){
                toReturn.add(t);
            }
        }
        profile_intermediate_tables(toReturn);
        return toReturn;
    }

}