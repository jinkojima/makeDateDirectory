import static java.util.Calendar.*

makeDateDirectory(parseArgs(args))

//引数処理
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
    //コピー対象ファイルの有無フラグ
    if (argsMap.copyFile != null) {
        this.hasCopyFile = true
        this.copyFile = new File(argsMap.copyFile)
    } else {
        this.hasCopyFile = false
    }       

    //開始日の設定
    def startCal = Calendar.instance
    def m = [:]
    //引数はStringで取得しているのでIntに変換
    m[YEAR] = argsMap.startYEAR.toInteger()
    m[MONTH] = argsMap.startMONTH.toInteger() - 1   //CalendarのMONTHは1月=0のため調整
    m[DATE] = argsMap.startDATE.toInteger()
    startCal.set(m)
    println "startDate:"+startCal.format("yyyy-MM-dd")
    
    //終了日の設定
    def endCal = Calendar.instance
    def n = [:]
    //引数はStringで取得しているのでIntに変換
    n[YEAR] = argsMap.endYEAR.toInteger()
    n[MONTH] = argsMap.endMONTH.toInteger() -1      //CalendarのMONTHは1月=0のため調整
    n[DATE] = argsMap.endDATE.toInteger()
    endCal.set(n)
    println "endDate:"+endCal.format("yyyy-MM-dd")

    //出力先パスの設定
    def workPath = argsMap.outputPath
    println "outputPath:"+workPath

    //開始日と終了日の評価
    def dateDiff = startCal.compareTo(endCal)
    //dateDiff
    // =0   同じ日付
    // >0   startが未来
    // <0   startが過去
    if (dateDiff > 0) {
        System.err.println "because [startDate > endDate], this script has shut down."
        System.exit 1
    } else {
    //開始日が終了日より過去であれば、終了日まで処理実行
        while (dateDiff <= 0){

            def dateDir = workPath+"/"+startCal.format("yyyy-MM-dd")
                //出力先パス配下に日付フォルダを作成
                new File(dateDir).mkdirs()
                //コピーファイルがある場合は、日付フォルダ配下にコピー
                if(hasCopyFile) {
                    //日付フォルダ配下にコピー元ファイル名を追記した文字列を作っておく
                    copiedFile = dateDir+"/"+copyFile.getName()
                    new File(copiedFile) << copyFile
                }
                startCal = startCal.next()
                dateDiff = startCal.compareTo(endCal)
        }
    }
}

