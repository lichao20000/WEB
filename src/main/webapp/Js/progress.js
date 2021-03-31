
function CProgress(progressIdStr, min, max, pos)
{
    this.progressIdStr = progressIdStr;
    this.progressId = document.getElementById(this.progressIdStr);
    this.barIdStr = progressIdStr + "_bar";
    this.barId = null;
    
    this.min = (min>=0)?min:0;
    this.max = (max>=min)?max:min;
    this.pos = (pos>=min && pos<=max)?pos:min;
    this.step = 1;
    
    this.progressWidth = 100;
    this.progressHeight = 15;
    
    this.Create = Create;

    this.SetStep = SetStep;
    this.SetPos = SetPos;
    this.Inc = Inc;
    this.Desc = Desc;
}

function Create()
{
    if (document.all)
    {
        this.progressId.style.width = this.progressWidth+2;
    }
    else
    {
        this.progressId.style.width = this.progressWidth;
    }
    this.progressId.style.height = this.progressHeight;
    this.progressId.style.fontSize = this.progressHeight;
    this.progressId.style.border = "1px solid #000000";
    this.progressId.innerHTML = "<div id=\"" + this.barIdStr + "\" style=\"background-color:#006699;height:100%;\"></div>";
    this.barId = document.getElementById(this.barIdStr);
    this.SetPos(this.pos);
}

function SetStep(step)
{
    this.step = step;
}

function SetPos(pos)
{
    pos = (pos<=this.max)?pos:this.max;
    pos = (pos>=this.min)?pos:this.min;
    this.barId.style.width = (this.progressWidth*pos)/this.max;
}

function Inc()
{
    this.pos += this.step;
    this.SetPos(this.pos);
}

function Desc()
{
    this.pos -= this.step;
    this.SetPos(this.pos);
}

var interval;
var progress;

function startProgress(){
	progress = new CProgress("progress", 0, 1000, 50);
	progress.progressWidth = 500;
	progress.Create();
	document.getElementById("progress").style.display="";
	interval = window.setInterval("progress.Inc();", 10);
}

function stopProgress(){
	window.clearInterval(interval);
	document.getElementById("progress").style.display="none";
}
