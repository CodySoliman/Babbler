<#include "header.ftl">

<form method="POST">
    <fieldset class="noborder">
        <div>
            <input type="search" id="searchField" name="searchText"></input>
            <button type="submit" class="action-button" id="search-btn" name="searchBabble">Search babble</button>
        </div>
    </fieldset>
</form>

<#if resultBabbles??>
<table id="results-header">
         <tr>
            <td>Search results for: ${searchText}</td>
         </tr>
      </table>
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