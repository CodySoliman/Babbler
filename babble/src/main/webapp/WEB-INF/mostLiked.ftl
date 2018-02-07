<#include "header.ftl">

<h2 id="head-title">Top 5 most liked Babbles</h2>

<#if resultBabbles??>
      <#list resultBabbles as babble>
      <table id="babble-box-woi" class="babble-border">
         <tr>
            <td id="babble-creator"><a href="babble_details?id=${babble.id}">${babble.creatorName} @${babble.creatorUsername}</a></td>
         </tr>
         <tr>
            <td id="babble-text">${babble.text}</td>
         </tr>
         <tr>
            <td id="babble-count">${babble.likeCount} Likes / ${babble.dislikeCount} Dislikes / ${babble.rebabbleCount} Rebabbles</td>
         </tr>
         <tr>
            <td id="babble-time">${babble.formattedCreationTime}</td>
         </tr>
      </table>
      </#list>
</#if>
<#include "footer.ftl">