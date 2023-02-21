package crm.security

import spock.lang.Specification

class AccessRoleTest extends Specification {
  
  def "should not allow to assign super admin role"() {
    given:
        def ownedRoles = Set.of(AccessRole.SUPER_ADMIN)
        def toAssign = Set.of(AccessRole.CHANGE_USER, AccessRole.SUPER_ADMIN)
    when:
        def result = AccessRole.canAssignRoles(ownedRoles, toAssign)
    then:
        !result
  }
  
  def "should allow to assign roles when"(AccessRole[] ownerRoles, AccessRole[] toAssign) {
    when:
        def result = AccessRole.canAssignRoles(Set.of(ownerRoles), Set.of(toAssign))
    then:
        result
    where:
        ownerRoles << [
            [AccessRole.SUPER_ADMIN],
            [AccessRole.SUPER_ADMIN],
            [AccessRole.ORG_ADMIN],
            [AccessRole.ORG_ADMIN],
            [AccessRole.VIEW_USER_GROUPS, AccessRole.CREATE_USER_GROUP, AccessRole.DELETE_USER_GROUP],
        ]
        toAssign << [
            [AccessRole.ORG_ADMIN],
            [AccessRole.CHANGE_USER, AccessRole.USER_GROUP_MANAGER],
            [AccessRole.ORG_ADMIN, AccessRole.VIEW_USERS, AccessRole.VIEW_USER_GROUPS],
            [AccessRole.CREATE_USER, AccessRole.DELETE_USER, AccessRole.CREATE_USER_GROUP],
            [AccessRole.VIEW_USER_GROUPS, AccessRole.DELETE_USER_GROUP],
        ]
  }
  
  def "should mot allow to assign roles when"(AccessRole[] ownerRoles, AccessRole[] toAssign) {
    when:
        def result = AccessRole.canAssignRoles(Set.of(ownerRoles), Set.of(toAssign))
    then:
        !result
    where:
        ownerRoles << [
            [],
            [AccessRole.SUPER_ADMIN],
            [AccessRole.SUPER_ADMIN],
            [AccessRole.ORG_ADMIN],
            [AccessRole.VIEW_USER_GROUPS, AccessRole.CREATE_USER_GROUP, AccessRole.DELETE_USER_GROUP],
            [AccessRole.VIEW_USER_GROUPS, AccessRole.CREATE_USER_GROUP, AccessRole.DELETE_USER_GROUP],
        ]
        toAssign << [
            [AccessRole.CHANGE_USER],
            [AccessRole.SUPER_ADMIN],
            [AccessRole.CHANGE_USER, AccessRole.USER_GROUP_MANAGER, AccessRole.SUPER_ADMIN],
            [AccessRole.ORG_ADMIN, AccessRole.SUPER_ADMIN, AccessRole.VIEW_USER_GROUPS],
            [AccessRole.ORG_ADMIN, AccessRole.DELETE_USER_GROUP],
            [AccessRole.SUPER_ADMIN, AccessRole.DELETE_USER_GROUP],
        ]
  }
}
