package net.unit8.occupypub.config;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseTable;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.tools.schemaframework.IndexDefinition;

/**
 * @author kawasima
 */
public class SnakeCaseNameMapping implements SessionCustomizer {
    @Override
    public void customize(Session session) throws Exception {
        for (ClassDescriptor descriptor : session.getDescriptors().values()) {
            if (!descriptor.getTables().isEmpty() && descriptor.getAlias().equalsIgnoreCase(descriptor.getTableName())) {
                String tableName = addUnderscores(descriptor.getAlias()).toUpperCase();
                System.out.println(descriptor.getAlias() + ":" + tableName);
                descriptor.setTableName(tableName);

                DatabaseTable databaseTable = descriptor.getTables().get(0);
                for (IndexDefinition indexDef : databaseTable.getIndexes()) {
                    indexDef.setTargetTable(tableName);
                }
            }

            for (DatabaseMapping mapping : descriptor.getMappings()) {
                if (mapping.getField() != null
                        && !mapping.getAttributeName().isEmpty()
                        && mapping.getField().getName().equalsIgnoreCase(mapping.getAttributeName())) {
                    mapping.getField().setName(
                            addUnderscores(mapping.getAttributeName()).toUpperCase()
                    );
                }
            }
        }
    }

    protected String addUnderscores(String name) {
        StringBuilder buf = new StringBuilder(name.replace('.', '_'));
        for (int i=1; i< buf.length() - 1; i++) {
            if (Character.isLowerCase(buf.charAt(i - 1))
                    && Character.isUpperCase(buf.charAt(i))
                    && Character.isLowerCase(buf.charAt(i+1))) {
                buf.insert(i++, '_');
            }
        }
        return buf.toString();
    }
}
