$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$fx = Join-Path $root 'lib\javafx-sdk-21.0.8\lib'
$mysql = Join-Path $root 'lib\mysql-connector-j-9.4.0.jar'
if (!(Test-Path (Join-Path $fx 'javafx.controls.jar'))) { throw 'JavaFX not found. Put JavaFX SDK 21.0.8 in lib\javafx-sdk-21.0.8' }
if (!(Test-Path $mysql)) { throw 'MySQL Connector/J not found in lib.' }
if (Test-Path (Join-Path $root 'out')) { Remove-Item (Join-Path $root 'out') -Recurse -Force }
New-Item (Join-Path $root 'out') -ItemType Directory | Out-Null
Copy-Item (Join-Path $root 'resources') (Join-Path $root 'out') -Recurse
$sources = Get-ChildItem (Join-Path $root 'src') -Filter *.java -Recurse | ForEach-Object { $_.FullName }
& javac --module-path $fx --add-modules javafx.controls -cp $mysql -d (Join-Path $root 'out') $sources
& java --module-path $fx --add-modules javafx.controls -cp "$(Join-Path $root 'out');$mysql" com.smartspend.Main
