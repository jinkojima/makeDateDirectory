import static java.util.Calendar.*

makeDateDirectory(parseArgs(args))

//$B0z?t=hM}(B
def parseArgs(args) {
    switch (args.size()) {
        case 7:
            return [
                startYEAR:args[0], 
                startMONTH:args[1],
                startDATE:args[2],
                endYEAR:args[3],
                endMONTH:args[4],
                endDATE:args[5],
                outputPath:args[6],
                copyFile:null
            ]
        case 8: 
            return [
                startYEAR:args[0], 
                startMONTH:args[1],
                startDATE:args[2],
                endYEAR:args[3],
                endMONTH:args[4],
                endDATE:args[5],
                outputPath:args[6],
                copyFile:args[7]
            ]
        default:
            System.err.println "usage: groovy makeDateDirectory startYEAR startMONTH startDATE endYEAR endMONTH endDATE outputPath (copyFile)"
            System.exit 1
    }
}

def makeDateDirectory(argsMap) {
    //$B%3%T!<BP>]%U%!%$%k$NM-L5%U%i%0(B
    if (argsMap.copyFile != null) {
        this.hasCopyFile = true
        this.copyFile = new File(argsMap.copyFile)
    } else {
        this.hasCopyFile = false
    }       

    //$B3+;OF|$N@_Dj(B
    def startCal = Calendar.instance
    def m = [:]
    //$B0z?t$O(BString$B$G<hF@$7$F$$$k$N$G(BInt$B$KJQ49(B
    m[YEAR] = argsMap.startYEAR.toInteger()
    m[MONTH] = argsMap.startMONTH.toInteger() - 1   //Calendar$B$N(BMONTH$B$O(B1$B7n(B=0$B$N$?$aD4@0(B
    m[DATE] = argsMap.startDATE.toInteger()
    startCal.set(m)
    println "startDate:"+startCal.format("yyyy-MM-dd")
    
    //$B=*N;F|$N@_Dj(B
    def endCal = Calendar.instance
    def n = [:]
    //$B0z?t$O(BString$B$G<hF@$7$F$$$k$N$G(BInt$B$KJQ49(B
    n[YEAR] = argsMap.endYEAR.toInteger()
    n[MONTH] = argsMap.endMONTH.toInteger() -1      //Calendar$B$N(BMONTH$B$O(B1$B7n(B=0$B$N$?$aD4@0(B
    n[DATE] = argsMap.endDATE.toInteger()
    endCal.set(n)
    println "endDate:"+endCal.format("yyyy-MM-dd")

    //$B=PNO@h%Q%9$N@_Dj(B
    def workPath = argsMap.outputPath
    println "outputPath:"+workPath

    //$B3+;OF|$H=*N;F|$NI>2A(B
    def dateDiff = startCal.compareTo(endCal)
    //dateDiff
    // =0   $BF1$8F|IU(B
    // >0   start$B$,L$Mh(B
    // <0   start$B$,2a5n(B
    if (dateDiff > 0) {
        System.err.println "because [startDate > endDate], this script has shut down."
        System.exit 1
    } else {
    //$B3+;OF|$,=*N;F|$h$j2a5n$G$"$l$P!"=*N;F|$^$G=hM}<B9T(B
        while (dateDiff <= 0){

            def dateDir = workPath+"/"+startCal.format("yyyy-MM-dd")
                //$B=PNO@h%Q%9G[2<$KF|IU%U%)%k%@$r:n@.(B
                new File(dateDir).mkdirs()
                //$B%3%T!<%U%!%$%k$,$"$k>l9g$O!"F|IU%U%)%k%@G[2<$K%3%T!<(B
                if(hasCopyFile) {
                    //$BF|IU%U%)%k%@G[2<$K%3%T!<85%U%!%$%kL>$rDI5-$7$?J8;zNs$r:n$C$F$*$/(B
                    copiedFile = dateDir+"/"+copyFile.getName()
                    new File(copiedFile) << copyFile
                }
                startCal = startCal.next()
                dateDiff = startCal.compareTo(endCal)
        }
    }
}

