(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
function Artwork(e){return this.node=create("img",{src:e.itunes.image,alt:e.title,className:"pb-embed--info-artwork"}),this.components={},this}var create=require("../utils/create");module.exports=Artwork;

},{"../utils/create":7}],2:[function(require,module,exports){
function Entry(e){return this.node=create("div",{className:"pb-embed--entry",dataset:{guid:e.guid}}),this.components={},this.components.player=new Player(e),this.components.title=this.node.appendChild(create("h3",{className:"pb-embed--entry-title"})),this.node.appendChild(this.components.player.node),this.components.meta=this.node.appendChild(create("div",{className:"pb-embed--entry-meta"})),this.components.meta.appendChild(create("span",{textContent:new Date(e.pubDate).toLocaleDateString(),className:"pb-embed--entry-date"})),this.components.description=this.components.meta.appendChild(create("p",{innerHTML:e.content,className:"pb-embed--entry-description"})),this.components.title.appendChild(create("a",{textContent:e.title,href:e.link,target:"_blank",className:"pb-embed--entry-link"})),this}var create=require("../utils/create"),Player=require("./player");module.exports=Entry;

},{"../utils/create":7,"./player":4}],3:[function(require,module,exports){
function Info(e){return this.node=create("div",{className:"pb-embed--info"}),this.components={},this.components.artwork=new Artwork(e),this.node.appendChild(this.components.artwork.node),this.components.title=this.node.appendChild(create("h2",{textContent:e.title,className:"pb-embed--info-title"})),this.components.description=this.node.appendChild(create("p",{textContent:e.description,className:"pb-embed--info-description"})),this.components.subscribe=this.node.appendChild(create("a",{textContent:"Subscribe",href:e.link,target:"_blank",className:"pb-embed--info-subscribe"})),this}var create=require("../utils/create"),Artwork=require("./artwork");module.exports=Info;
},{"../utils/create":7,"./artwork":1}],4:[function(require,module,exports){
function Player(e){return this.node=create("audio",{src:e.enclosure.url,className:"pb-embed--entry-player",controls:!0}),this.components={},this}var create=require("../utils/create");module.exports=Player;

},{"../utils/create":7}],5:[function(require,module,exports){
!function(e,n){function t(){var e=[],t=0;for(e=n.querySelectorAll(".pb-embed"),t=0;t<e.length;t++)r(e[t])}function r(e,n){return n=n||e.dataset.feed,new i(e,n)}var i=require("./pb");e.pb={init:t,createEmbed:r},t()}(window,window.document);
},{"./pb":6}],6:[function(require,module,exports){
function PB(e,t){return this.node=e,this.feed=t,this.data={},this.components={info:{},entries:[]},this.reload(),this}var Info=require("./components/info"),Entry=require("./components/entry"),request=require("./utils/jsonp");module.exports=PB,PB.prototype.reload=function(){request("https://www.narro.co/api/v1/parser/feed?url="+encodeURIComponent(this.feed),function(e){e&&e.data?(this.data=e.data[0],this.render()):(this.node.textContent="Error loading embedded podcast",this.node.className+="pb-embed-error")}.bind(this))},PB.prototype.render=function(){var e,t=0;t=parseInt(this.node.dataset.limit,10)||this.data.entries.length,t=Math.min(t,this.data.entries.length),this.node.innerHTML="",this.components.info=new Info(this.data),this.node.appendChild(this.components.info.node);for(var n=0;n<t;n++)e=new Entry(this.data.entries[n]),this.components.entries.push(e),this.node.appendChild(e.node)};
},{"./components/entry":2,"./components/info":3,"./utils/jsonp":8}],7:[function(require,module,exports){
module.exports=function(e,r){var o=document.createElement(e);for(var t in r)if("object"==typeof r[t])for(var n in r[t])o[t][n]=r[t][n];else o[t]=r[t];return o};
},{}],8:[function(require,module,exports){
module.exports=function(n,a){function c(n,a,c){var l=n+"&"+(c||"callback")+"="+a,o=document.createElement("script");o.src=l,document.body.appendChild(o)}window.pbCallbacks=window.pbCallbacks||{cntr:0},function(n,a,l){var o="fn"+window.pbCallbacks.cntr++;window.pbCallbacks[o]=function(){delete window.pbCallbacks[o];var n=[a,arguments[0]];l.apply(this,n)},c(n,"pbCallbacks."+o)}(n,n,function(n,c){a(c)})};

},{}]},{},[5]);
