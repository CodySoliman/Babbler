<#include "header.ftl">
<#if found>
<nav>
   <ul id="navigation">
      <li><a href="babble_search" class="button">Search Babble</a></li>
      <#if userId != loggedInUser>
      <#if !blocked><li><a href="user?action=${follow}" class="button">${follow}</a></li></#if>
      <li><a href="user?action=${block}" class="button">${block}</a></li>
      <li><a href="user_messages?id=${userId}" class="button">Send Message</a></li>
      </#if>
   </ul>
</nav>


<table id="message-box">
         <tr>
           <#if message??><td class="success" id="user-actions-msg">${message}</td></#if>
         </tr>
      </table>
      


<div id="userbox">
   <div id="userInfo">
      <table>
         <tr>
            <td>Benutzer: </td>
            <td>@${user_username}</td>
         </tr>
         <tr>
            <td>Name: </td>
            <td>${user_name}</td>
         </tr>
         <tr>
            <td>Status: </td>
            <td>${user_status}</td>
         </tr>
      </table>
   </div>
   <div id="profilePic_wrapper">
      <img id="profile_pic" src="${user_pic}"/>
   </div>
</div>
<#if babbles?has_content || blocked>
<div>
<#if !blocked>
<ul id="navigation" class="right-btn" <#if babbles[0].interactionType != "optional"> style="margin-bottom:0;" </#if>>
      <li><a href="babble_create" id="new-babble-btn">New Babble</a></li>
   </ul>
   </#if>
   <#if !blocked>
   <#list babbles as babble>
   <div id="outer-border-babble">
   <#if babble.interactionType != "optional">
      <table>
         <tr>
            <td>@${user_username} hat ${babble.interactionType} am ${babble.formattedInteractionTime}</td>
         </tr>
      </table>
      </#if>
      <table <#if babble.interactionType == "optional"> id="babble-box-woi" </#if> class="babble-border">
         <tr>
            <td id="babble-creator"><a href="babble_details?id=${babble.id}">${babble.creatorName} @${babble.creatorUsername}</a></td>
         </tr>
         <tr>
            <td id="babble-text">${babble.text}</td>
         </tr>
         <tr>
            <td id="babble-count">${babble.likeCount} Likes / ${babble.dislikeCount} Dislikes / ${babble.rebabbleCount} Rebabbles</td>
         </tr>
         <#if babble.interactionType != "optional">
         <tr>
            <td id="babble-time">${babble.formattedCreationTime}</td> <#-- Or ${babble.formattedInteractionTime} on demand -->
         </tr>
         <#else>
         <tr>
            <td id="babble-time">${babble.formattedCreationTime}</td>
         </tr>
         </#if>
      </table>
      </div>
      </#list>
      <#else>
      <p>Du wurdest blockiert<br/>Grund: ${block_reason}</p>
      </#if>
   </#if>
   <#else>
   <h1 id="page-head">Wrong username has been entered. User "${userId}" doesn't exist.</h1>
   <p><a href="\login">Click here</a> to try another username.</p>
   </#if>
   </body>
<#include "footer.ftl">