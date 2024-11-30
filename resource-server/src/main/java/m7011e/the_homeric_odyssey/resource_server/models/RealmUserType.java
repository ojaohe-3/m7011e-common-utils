package m7011e.the_homeric_odyssey.resource_server.models;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import java.util.function.Predicate;

/**
 * The `RealmUserType` enumeration defines various roles that a user can have within a realm.
 * Each role is associated with a specific role name.
 * <p>
 * This enum implements `Predicate<RealmUserType>` to provide functionality for evaluating
 * whether a given `RealmUserType` matches certain conditions.
 */
@Getter
public enum RealmUserType implements Predicate<RealmUserType> {
    ADMIN("ROLE_ADMIN"),
    CUSTOMER("ROLE_CUSTOMER"),
    VENDOR("ROLE_VENDOR"),
    SYSTEM("ROLE_SYSTEM"),
    READ("ROLE_READ"),
    WRITE("ROLE_WRITE");

    private final String roleName;

    RealmUserType(String roleName) {

        this.roleName = roleName;
    }

    /**
     * Evaluates this predicate on the given `RealmUserType`.
     * <p>
     * Always returns false in its current implementation.
     *
     * @param realmUserType the input argument of type `RealmUserType` to test against the predicate
     * @return `false` in all cases, as the current implementation does not perform any checks
     */
    @Override
    public boolean test(RealmUserType realmUserType) {
        return false;
    }

    /**
     * Tests if the given role name matches the role name of this `RealmUserType`.
     *
     * @param roleName the name of the role to be compared
     * @return true if the given role name matches the role name of this `RealmUserType`, false otherwise
     */
    public boolean test(String roleName) {
        return this.roleName.equals(roleName);
    }

    /**
     * Checks if the specified `realmUserType` is equal to the `RealmUserType`
     * corresponding to the given `roleName`.
     *
     * @param realmUserType the `RealmUserType` instance to compare
     * @param roleName      the role name to be converted to a `RealmUserType` for comparison
     * @return true if the specified `realmUserType` is equal to the `RealmUserType`
     * obtained from the given `roleName`, false otherwise
     */
    public boolean test(RealmUserType realmUserType, String roleName) {
        return realmUserType.equals(fromString(roleName));
    }

    /**
     * Converts a string representing a role name to the corresponding `RealmUserType` enum constant.
     *
     * @param roleName the name of the role as a string to be converted into a `RealmUserType`
     * @return the `RealmUserType` corresponding to the given role name, or null if there is
     * no constant with the specified role name
     */
    public static RealmUserType fromString(String roleName) {
        return EnumUtils.getEnum(RealmUserType.class, roleName);
    }
}
