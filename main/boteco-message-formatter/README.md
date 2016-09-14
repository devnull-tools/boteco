# Boteco Message Formatter

This is the implementation of the message formatter for boteco. Since every chat
platform has its own format conventions, we need a standard way of marking a
format. This bundle contains the patterns for every format boteco supports.

The implementation is based on a `[tag]sentence[/tag]` pattern:

- `[a]`: marks an accent format
- `[aa]`: marks an alternative accent format
- `[v]`: marks a value format
- `[p]`: marks a positive format (for using with numbers)
- `[n]`: marks a negative format (for using with numbers)
- `[t]`: marks a tag format (for using as tags or labels)
- `[e]`: marks an error format (for error messages)
- `[l]`: marks a link format (`title <link>`)
