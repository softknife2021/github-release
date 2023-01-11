<#-- $name=csv-table;description=default post pet;version=0.1$ -->
<#assign body = JsonUtil.convertJsonToMap(input)>
application,commit message,Author,Committed Date,Effected Files,Jira Id, Jira Status
<#list body.entrySet() as entry>
    <#assign values = entry.value>
    <#list values as v>
    "${entry.key}","${v.commitMessage}","${v.author}","${v.committedDate}","${v.effectedFile}","${v.jiraId!'Not Set'}","${v.jiraStatus!'Not Set'}"
</#list>
</#list>