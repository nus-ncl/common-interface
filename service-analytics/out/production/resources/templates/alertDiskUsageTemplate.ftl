Dear NCL Operations,

The following users are using more than ${dangerThreshold} of disk space as of ${timestamp}:

<#list userSpaces as user>
    * ${user}
</#list>

Please inform the users to remove their files immediately or we may have to remove for them when necessary.

The following projects are using more than ${dangerThreshold} of disk space as of ${timestamp}:

<#list projSpaces as proj>
    * ${proj}
</#list>

Please inform the owners to remove their files immediately or we may have to remove for them when necessary.

For the full list of disk space usage, please log into https://ncl.sg/admin/diskspace.

Regards,
Testbed Admin
