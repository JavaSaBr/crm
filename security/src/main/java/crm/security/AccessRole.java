package crm.security;

import static com.ss.rlib.common.util.array.ArrayFactory.toArray;
import static java.util.stream.Collectors.toUnmodifiableSet;

import crm.base.util.EnumWithIdAndDescription;
import crm.base.util.ExtendedEnumMap;
import com.ss.rlib.common.util.ArrayUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.LongStream;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AccessRole implements EnumWithIdAndDescription {
  SUPER_ADMIN(1, "Super administrator"),
  ORG_ADMIN(2, "Organization administrator"),
  CURATOR(3, "Curator"),

  CREATE_USER(100, "Create user"),
  CHANGE_USER(101, "Change user"),
  VIEW_USERS(102, "View users"),
  DELETE_USER(103, "Delete user"),

  USER_MANAGER(4, "User manager", toArray(CREATE_USER, CHANGE_USER, VIEW_USERS, DELETE_USER)),

  CREATE_USER_GROUP(200, "Create user group"),
  CHANGE_USER_GROUP(201, "Change user group"),
  VIEW_USER_GROUPS(202, "View user groups"),
  DELETE_USER_GROUP(203, "Delete user group"),

  USER_GROUP_MANAGER(5,
      "User manager",
      toArray(CREATE_USER_GROUP, CHANGE_USER_GROUP, VIEW_USER_GROUPS, DELETE_USER_GROUP));

  private static final ExtendedEnumMap<AccessRole> ENUM_MAP = new ExtendedEnumMap<>(AccessRole.class);

  public static final AccessRole[] EMPTY_ARRAY = new AccessRole[0];

  public static @Nullable AccessRole withId(long id) {
    return ENUM_MAP.withId((int) id);
  }

  public static @NotNull AccessRole required(long id) {
    return ENUM_MAP.required((int) id);
  }

  public static @NotNull Set<AccessRole> toSet(long @Nullable [] roles) {

    if (ArrayUtils.isEmpty(roles)) {
      return Set.of();
    }

    return LongStream
        .of(roles)
        .mapToObj(AccessRole::required)
        .collect(toUnmodifiableSet());
  }

  public static boolean canAssignRoles(@NotNull Set<AccessRole> ownedRoles, @NotNull Set<AccessRole> toAssign) {
    if (toAssign.contains(AccessRole.SUPER_ADMIN)) {
      return false;
    } else if (ownedRoles.contains(AccessRole.SUPER_ADMIN)) {
      return true;
    } else if (ownedRoles.contains(AccessRole.ORG_ADMIN)) {
      return !toAssign.contains(AccessRole.SUPER_ADMIN);
    } else {
      return ownedRoles.containsAll(toAssign);
    }
  }

  long id;
  @NotNull String description;
  @NotNull AccessRole[] subRoles;

  AccessRole(long id, @NotNull String description) {
    this.id = id;
    this.description = description;
    this.subRoles = new AccessRole[0];
  }
}
