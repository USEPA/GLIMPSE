% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/zenergy_L119.solar.R
\name{module_energy_L119.solar}
\alias{module_energy_L119.solar}
\title{module_energy_L119.solar}
\usage{
module_energy_L119.solar(command, ...)
}
\arguments{
\item{command}{API command to execute}

\item{...}{other optional parameters, depending on command}
}
\value{
Depends on \code{command}: either a vector of required inputs,
a vector of output names, or (if \code{command} is "MAKE") all
the generated outputs: \code{L119.Irradiance_rel_R}. The corresponding file in the
original data system was \code{LA119.solar.R} (energy level1).
}
\description{
Computes relative average irradiance and dni (direct normal irradiance) by GCAM region for solar sector.
}
\details{
The chunk computes relative average irradiance and dni (direct normal irradiance) from Smith_irradiance_ctry_kwh.csv
by GCAM region for solar sector. The average irradiance and dni is relative to USA values.
}
\author{
LF November 2017
}
