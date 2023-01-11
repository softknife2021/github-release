<#-- $name=markup-table;description=generate wiki table;version=0.1$ -->
<#assign body = JsonUtil.convertJsonToMap(input)>
<#list body.entrySet() as entry>
    ||application||commit message||Author||Effected Files||
    <#assign values = entry.value>
    <#list values as v>
    |${entry.key}|${v.commitMessage}|${v.author}|${v.effectedFile}|
</#list>
</#list>