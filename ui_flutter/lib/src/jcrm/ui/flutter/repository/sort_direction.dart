
enum SortDirection {
  asc,
  desc;

  static SortDirection forAscending(bool ascending) {
    return ascending ? SortDirection.asc : SortDirection.desc;
  }
}