% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/zaglu_L161.ag_R_C_Y_GLU_irr.R
\name{module_aglu_L161.ag_R_C_Y_GLU_irr}
\alias{module_aglu_L161.ag_R_C_Y_GLU_irr}
\title{module_aglu_L161.ag_R_C_Y_GLU_irr}
\usage{
module_aglu_L161.ag_R_C_Y_GLU_irr(command, ...)
}
\arguments{
\item{command}{API command to execute}

\item{...}{other optional parameters, depending on command}
}
\value{
Depends on \code{command}: either a vector of required inputs,
a vector of output names, or (if \code{command} is "MAKE") all
the generated outputs: \code{L161.ag_irrProd_Mt_R_C_Y_GLU}, \code{L161.ag_rfdProd_Mt_R_C_Y_GLU}, \code{L161.ag_irrHA_bm2_R_C_Y_GLU}, \code{L161.ag_rfdHA_bm2_R_C_Y_GLU}, \code{L161.ag_irrYield_kgm2_R_C_Y_GLU}, \code{L161.ag_rfdYield_kgm2_R_C_Y_GLU}, \code{L161.ag_irrHA_frac_R_C_GLU}. The corresponding file in the
original data system was \code{LB161.ag_R_C_Y_GLU_irr.R} (aglu level1).
}
\description{
Calculates irrigated and rainfed agriculture production, harvested area and yields by GCAM region / commodity / GLU / year.
}
\details{
This chunk combines FAO annual data and GTAP disaggregated irrigated vs. rainfed data to compute irrigated and rainfed
agriculture production, harvested area and yields by GCAM region / commodity / GLU / year. The same irrigated and rainfed fraction
is applied to all historical years for each commodity, region and GLU.
}
\author{
RC August 2017
}
