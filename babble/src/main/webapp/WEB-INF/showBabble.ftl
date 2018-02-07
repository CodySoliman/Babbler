<#include "header.ftl">


<table id="message-box">
         <tr>
            <td class="success"><#if submitMessage??>${submitMessage}</#if></td>
         </tr>
      </table>
      

<table id="babble-box-woi" class="babble-border">
         <tr>
            <td id="babble-creator"><a href="/user?id=${creator}">${creatorName} @${creator}</a></td>
         </tr>
         <tr>
            <td id="babble-text">${text}</td>
         </tr>
         <tr>
            <td id="babble-count">${likes} Likes / ${dislikes} Dislikes / ${rebabbles} Rebabbles</td>
         </tr>
         <tr>
            <td id="babble-time">${creationTime}</td>
         </tr>
      </table>
      
    <ul id="navigation" class="babble-action-btns">
      <li <#if liked == "liked"> id="like-active" </#if>><a href="/babble_details?action=like">Like</a></li>
      <li <#if liked == "disliked"> id="like-active" </#if>><a href="/babble_details?action=dislike">Dislike</a></li>
      <li <#if rebabbled == "rebabbled"> id="rebabble-active" </#if>><a href="/babble_details?action=rebabble">Rebabble</a></li>
   </ul>
   
   <#if babbleOwner>
   <ul id="navigation" class="babble-delete-btn">
      <li id="delete-btn"><a href="/babble_details?action=delete">Delete Babble</a></li>
   </ul>
   </#if>
   
   <#include "footer.ftl">