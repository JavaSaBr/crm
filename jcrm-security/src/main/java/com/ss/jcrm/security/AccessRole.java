package com.ss.jcrm.security;

import static com.ss.rlib.common.util.array.ArrayFactory.toArray;
import static java.util.stream.Collectors.toUnmodifiableSet;

import com.ss.jcrm.base.utils.WithId;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ObjectUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AccessRole implements WithId {
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

  USER_GROUP_MANAGER(
      5,
      "User manager",
      toArray(CREATE_USER_GROUP, CHANGE_USER_GROUP, VIEW_USER_GROUPS, DELETE_USER_GROUP)),
  ;

  public static final AccessRole[] EMPTY_ARRAY = new AccessRole[0];

  private static final AccessRole[] ID_TO_ROLE;

  static {

    var length = Stream
        .of(AccessRole.values())
        .mapToInt(value -> (int) value.id)
        .max()
        .orElse(0);

    ID_TO_ROLE = new AccessRole[length + 1];

    for (var accessRole : AccessRole.values()) {
      ID_TO_ROLE[(int) accessRole.id] = accessRole;
    }
  }

  public static @Nullable AccessRole of(long id) {
    return of((int) id);
  }

  public static @Nullable AccessRole of(int id) {

    if (id < 0 || id >= ID_TO_ROLE.length) {
      return null;
    }

    return ID_TO_ROLE[id];
  }

  public static @NotNull AccessRole require(long id) {
    return require((int) id);
  }

  public static @NotNull AccessRole require(int id) {

    if (id < 0 || id >= ID_TO_ROLE.length) {
      throw new IllegalArgumentException("An access role's id cannot be < 0 or > " + (ID_TO_ROLE.length - 1));
    }

    return ObjectUtils.notNull(
        ID_TO_ROLE[id],
        id,
        value -> new IllegalStateException("Can't find an access role by the id " + value));
  }

  public static @NotNull Set<AccessRole> toSet(long @Nullable [] roles) {

    if (ArrayUtils.isEmpty(roles)) {
      return Set.of();
    } else {
      return LongStream
          .of(roles)
          .mapToObj(AccessRole::require)
          .collect(toUnmodifiableSet());
    }
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
  @NotNull String name;
  @NotNull AccessRole[] subRoles;

  AccessRole(long id, @NotNull String name) {
    this.id = id;
    this.name = name;
    this.subRoles = new AccessRole[0];
  }
}
