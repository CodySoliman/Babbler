<#include "header.ftl">

<#if submitMessage??>
<table id="messageBox">
         <tr>
            <td id="submitMessage" class="success">${submitMessage}</td>
         </tr>
      </table>
     </#if>
     
     <#include "footer.ftl">