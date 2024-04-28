package rest2soap;

import io.micronaut.core.annotation.TypeHint;
import org.apache.axiom.locator.DefaultOMMetaFactoryLocator;
import org.apache.axiom.om.impl.llom.factory.OMLinkedListMetaFactory;
import org.apache.axiom.om.impl.llom.factory.OMLinkedListMetaFactoryLoader;

@TypeHint(
        value = {
                DefaultOMMetaFactoryLocator.class,
                OMLinkedListMetaFactoryLoader.class,
                OMLinkedListMetaFactory.class
        },
        accessType = {
                TypeHint.AccessType.ALL_PUBLIC,
                TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS,
                TypeHint.AccessType.ALL_DECLARED_FIELDS,
                TypeHint.AccessType.ALL_DECLARED_METHODS,
        }
)
public class GraalvmAddon {
}
