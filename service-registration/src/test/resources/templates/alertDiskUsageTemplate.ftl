Dear Testbed Admin,
<#if userSpaces?has_content>

The following users are using more than ${dangerThreshold} of disk space as of ${timestamp}:

<#list userSpaces as user>
    * ${user.directory}
</#list>

Please inform the users to remove their files immediately or we may have to remove for them when necessary.
</#if>
<#if projSpaces?has_content>

The following projects are using more than ${dangerThreshold} of disk space as of ${timestamp}:

<#list projSpaces as proj>
    * ${proj.directory}
</#list>

Please inform the owners to remove their files immediately or we may have to remove for them when necessary.
</#if>

For the full list of disk space usage, please log into https://ncl.sg/admin/diskspace.

Regards,
NCL Operations
